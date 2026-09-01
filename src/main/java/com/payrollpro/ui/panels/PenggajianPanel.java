package com.payrollpro.ui.panels;

import com.payrollpro.dao.*;
import com.payrollpro.model.*;
import com.payrollpro.service.*;
import com.payrollpro.ui.MainFrame;
import com.payrollpro.util.*;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.YearMonth;
import java.util.List;

public class PenggajianPanel extends JPanel {

    private final MainFrame      mainFrame;
    private final PenggajianDAO  dao          = new PenggajianDAO();
    private final KaryawanDAO    karyawanDAO  = new KaryawanDAO();
    private final LemburDAO      lemburDAO    = new LemburDAO();
    private final CutiDAO        cutiDAO      = new CutiDAO();
    private final HariLiburDAO   hariLiburDAO = new HariLiburDAO();

    private JComboBox<Karyawan> cbKaryawan;
    private JComboBox<Integer>  cbBulan;
    private JSpinner            spnTahun;
    private JComboBox<String>   cbStatus;
    private JTextField          txtGolongan;

    // === PRORATA - sekarang terhubung ke UI ===
    private JCheckBox    chkProrata;
    private JDateChooser dcTglMasukBulan;
    private JLabel       lblProrataInfo;

    // Kalkulasi readonly
    private JTextField txtPokok, txtTjIstri, txtTjAnak, txtTransport;
    private JTextField txtLembur, txtPotCuti, txtAlphaInfo;
    private JTextField txtPph21, txtBpjsKes, txtBpjsJht, txtBpjsJp;
    private JTextField txtBruto, txtBersih, txtBpjsPerusahaan;

    private JLabel  lblLock;
    private JButton btnSimpan, btnUpdate, btnHapus, btnCetak, btnReset, btnLock;
    private JTable  table;
    private DefaultTableModel tableModel;
    private Penggajian selected;
    private boolean    isLocked = false;

    public PenggajianPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        build();
    }

    private void build() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Penggajian"));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(3, 5, 3, 5);
        gc.fill   = GridBagConstraints.HORIZONTAL;

        cbKaryawan    = new JComboBox<>();
        cbBulan       = new JComboBox<>(new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12});
        spnTahun      = new JSpinner(new SpinnerNumberModel(LocalDate.now().getYear(),2020,2100,1));
        cbStatus      = new JComboBox<>(new String[]{"Proses","Lunas"});
        txtGolongan   = ro("-");
        cbBulan.setSelectedItem(LocalDate.now().getMonthValue());

        // Prorata
        chkProrata      = new JCheckBox("Gaji Prorata (karyawan masuk/resign tengah bulan)");
        dcTglMasukBulan = new JDateChooser();
        dcTglMasukBulan.setDateFormatString("dd-MM-yyyy");
        dcTglMasukBulan.setEnabled(false);
        lblProrataInfo  = new JLabel(" ");
        lblProrataInfo.setForeground(new Color(30,100,180));
        lblProrataInfo.setFont(new Font("Segoe UI",Font.ITALIC,10));

        // Kalkulasi
        txtPokok=ro(""); txtTjIstri=ro(""); txtTjAnak=ro(""); txtTransport=ro("");
        txtLembur=ro(""); txtPotCuti=ro(""); txtAlphaInfo=ro("0 hari alpha");
        txtPph21=ro(""); txtBpjsKes=ro(""); txtBpjsJht=ro(""); txtBpjsJp=ro("");
        txtBruto=ro(""); txtBersih=ro(""); txtBpjsPerusahaan=ro("");

        lblLock = new JLabel(" ");
        lblLock.setForeground(new Color(180,40,40));
        lblLock.setFont(new Font("Segoe UI",Font.BOLD,11));

        // Events
        cbKaryawan.addActionListener(e   -> hitungGaji());
        cbBulan.addActionListener(e      -> hitungGaji());
        spnTahun.addChangeListener(e     -> hitungGaji());
        chkProrata.addActionListener(e   -> {
            dcTglMasukBulan.setEnabled(chkProrata.isSelected());
            hitungGaji();
        });
        dcTglMasukBulan.addPropertyChangeListener("date", e -> hitungGaji());

        // Layout rows
        r(form,gc,0,"Karyawan:",cbKaryawan,"Bulan/Tahun:",periodePanel());
        r(form,gc,1,"Golongan:",txtGolongan,"Status Bayar:",cbStatus);

        // Prorata row
        gc.gridy=2; gc.gridx=0; gc.gridwidth=2; form.add(chkProrata,gc); gc.gridwidth=1;
        gc.gridx=2; gc.weightx=0; form.add(new JLabel("Tgl Masuk di Bulan Ini:"),gc);
        gc.gridx=3; gc.weightx=1; form.add(dcTglMasukBulan,gc);

        gc.gridy=3; gc.gridx=0; gc.gridwidth=4; form.add(lblProrataInfo,gc); gc.gridwidth=1;
        gc.gridy=4; gc.gridx=0; gc.gridwidth=4; form.add(lblLock,gc); gc.gridwidth=1;
        gc.gridy=5; gc.gridx=0; gc.gridwidth=4; form.add(new JSeparator(),gc); gc.gridwidth=1;

        r(form,gc,6, "Gaji Pokok:",         txtPokok,    "Tunjangan Istri:",     txtTjIstri);
        r(form,gc,7, "Tunjangan Anak:",      txtTjAnak,   "Transport+Makan:",     txtTransport);
        r(form,gc,8, "Upah Lembur:",         txtLembur,   "Potongan Alpha/Cuti:", txtPotCuti);
        r(form,gc,9, "Detail Alpha:",        txtAlphaInfo,"Total Bruto:",         txtBruto);
        r(form,gc,10,"PPh 21 (PTKP auto):",  txtPph21,    "BPJS Kes Kary 1%:",   txtBpjsKes);
        r(form,gc,11,"BPJS JHT Kary 2%:",   txtBpjsJht,  "BPJS JP Kary 1%:",    txtBpjsJp);

        gc.gridy=12; gc.gridx=0; gc.gridwidth=2;
        form.add(new JLabel("Tanggungan Perusahaan (Kes4%+JHT3.7%+JP2%+JKK+JKM):"),gc);
        gc.gridx=2; gc.gridwidth=2; form.add(txtBpjsPerusahaan,gc); gc.gridwidth=1;

        // Gaji bersih highlight
        gc.gridy=13; gc.gridx=0; gc.gridwidth=4;
        JPanel bPanel = new JPanel(new BorderLayout(8,0));
        bPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(30,140,80,100)),
            BorderFactory.createEmptyBorder(6,8,6,8)));
        JLabel lBersih = new JLabel("GAJI BERSIH DITERIMA:");
        lBersih.setFont(new Font("Segoe UI",Font.BOLD,12));
        txtBersih.setFont(new Font("Segoe UI",Font.BOLD,13));
        txtBersih.setForeground(new Color(30,140,80));
        bPanel.add(lBersih,BorderLayout.WEST);
        bPanel.add(txtBersih,BorderLayout.CENTER);
        form.add(bPanel,gc); gc.gridwidth=1;

        // Tombol
        btnSimpan=new JButton("Simpan");  btnUpdate=new JButton("Update");
        btnHapus =new JButton("Hapus");   btnCetak =new JButton("Cetak Slip PDF");
        btnReset =new JButton("Reset");   btnLock  =new JButton("Kunci Periode");
        sb(btnSimpan,new Color(30,100,180),Color.WHITE);
        sb(btnHapus, new Color(180,40,40),Color.WHITE);
        sb(btnCetak, new Color(30,140,80),Color.WHITE);
        sb(btnLock,  new Color(120,60,20),Color.WHITE);
        sb(btnUpdate, new Color(255,140,0), Color.WHITE);
        sb(btnReset, new Color(120,120,120), Color.WHITE);

        gc.gridy=14; gc.gridx=0; gc.gridwidth=4;
        JPanel bp=new JPanel(new FlowLayout(FlowLayout.LEFT,4,0));
        bp.add(btnSimpan);bp.add(btnUpdate);bp.add(btnHapus);
        bp.add(btnCetak);bp.add(btnLock);bp.add(btnReset);
        form.add(bp,gc);

        String[] cols={"ID","Karyawan","Periode","Bruto","Potongan","Bersih","Prorata","Status","Lock"};
        tableModel=new DefaultTableModel(cols,0){
            @Override public boolean isCellEditable(int r,int c){return false;}};
        table=buildTable(tableModel);
        table.getSelectionModel().addListSelectionListener(e->{
            if(!e.getValueIsAdjusting()) onSelectRow();});

        JScrollPane scroll=new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder("Riwayat Penggajian"));

        add(form,BorderLayout.NORTH);
        add(scroll,BorderLayout.CENTER);

        btnSimpan.addActionListener(e->simpan());
        btnUpdate.addActionListener(e->update());
        btnHapus.addActionListener(e->hapus());
        btnCetak.addActionListener(e->cetakSlip());
        btnLock.addActionListener(e->lockPeriode());
        btnReset.addActionListener(e->reset());

        boolean admin=RoleManager.getInstance().isAdmin();
        btnSimpan.setEnabled(admin);btnUpdate.setEnabled(admin);
        btnHapus.setEnabled(admin);btnLock.setEnabled(admin);
    }

    public void refresh(){loadKaryawan();loadTable();}

    private void loadKaryawan(){
        new SwingWorker<List<Karyawan>,Void>(){
            @Override protected List<Karyawan> doInBackground() throws Exception{return karyawanDAO.getAllAktif();}
            @Override protected void done(){
                try{cbKaryawan.removeAllItems();for(Karyawan k:get())cbKaryawan.addItem(k);}
                catch(Exception e){showErr(e);}
            }
        }.execute();
    }

    private void loadTable(){
        int b=(int)cbBulan.getSelectedItem(),t=(int)spnTahun.getValue();
        new SwingWorker<List<Penggajian>,Void>(){
            @Override protected List<Penggajian> doInBackground() throws Exception{return dao.getAllByPeriode(b,t);}
            @Override protected void done(){
                try{
                    tableModel.setRowCount(0);
                    for(Penggajian p:get()){
                        tableModel.addRow(new Object[]{
                            p.getIdGaji(),p.getNamaKaryawan(),
                            ValidationUtil.getPeriodeLabel(p.getPeriodeBulan(),p.getPeriodeTahun()),
                            ValidationUtil.formatRupiah(p.getTotalBruto()),
                            ValidationUtil.formatRupiah(p.getTotalPotongan()),
                            ValidationUtil.formatRupiah(p.getGajiBersih()),
                            p.isProrata()?"Ya":"-",
                            p.getStatusBayar(),
                            p.isLocked()?"Terkunci":"-"
                        });
                    }
                }catch(Exception e){showErr(e);}
            }
        }.execute();
    }

    private void hitungGaji(){
        Karyawan k=(Karyawan)cbKaryawan.getSelectedItem();
        if(k==null||k.getGolongan()==null)return;
        int b=(int)cbBulan.getSelectedItem(),t=(int)spnTahun.getValue();
        boolean doPro=chkProrata.isSelected();
        txtGolongan.setText(k.getIdGolongan()+" - "+k.getGolongan().getNamaGolongan());

        new SwingWorker<Penggajian,Void>(){
            double totalLembur,potCuti;
            int hariAlpha,hariAktual,hariBulan;
            boolean prorata;

            @Override protected Penggajian doInBackground() throws Exception{
                totalLembur=lemburDAO.getTotalUpahByKaryawanBulan(k.getIdKaryawan(),b,t);
                hariAlpha=cutiDAO.getTotalAlpha(k.getIdKaryawan(),b,t);
                potCuti=(k.getGolongan().getGajiPokok()/25.0)*hariAlpha;
                prorata=false;
                if(doPro&&dcTglMasukBulan.getDate()!=null){
                    java.util.List<LocalDate> libur=hariLiburDAO.getAllTanggal();
                    LocalDate tglM=dcTglMasukBulan.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    ProRataCalculator.Result pr=ProRataCalculator.hitung(
                        k.getGolongan().getGajiPokok(),tglM,YearMonth.of(t,b).atEndOfMonth(),libur);
                    hariAktual=pr.hariKerjaAktual;hariBulan=pr.hariKerjaBulan;prorata=true;
                }
                return dao.getByKaryawanPeriode(k.getIdKaryawan(),b,t);
            }

            @Override protected void done(){
                try{
                    Penggajian ex=get();
                    isLocked=ex!=null&&ex.isLocked();
                    lblLock.setText(isLocked?"Periode ini sudah dikunci. Data tidak dapat diubah.":" ");
                    txtAlphaInfo.setText(hariAlpha+" hari alpha  -  Potongan: "+ValidationUtil.formatRupiah(potCuti));

                    Penggajian p=GajiCalculator.hitung(k,totalLembur,potCuti,prorata,hariAktual,hariBulan);

                    if(prorata)
                        lblProrataInfo.setText("Prorata: "+hariAktual+"/"+hariBulan+" hari kerja -> Gaji pokok: "+ValidationUtil.formatRupiah(p.getGajiPokok()));
                    else lblProrataInfo.setText(k.isProba()?"  Karyawan Probation: tunjangan istri & anak tidak berlaku":" ");

                    // Update UI
                    String ptkp=PPh21Calculator.getKodePTKP(k.isMenikah(),k.getJumlahAnak());
                    txtPokok.setText(ValidationUtil.formatRupiah(p.getGajiPokok())+(prorata?" (prorata)":""));
                    txtTjIstri.setText(ValidationUtil.formatRupiah(p.getTunjanganIstri()));
                    txtTjAnak.setText(ValidationUtil.formatRupiah(p.getTunjanganAnak()));
                    txtTransport.setText(ValidationUtil.formatRupiah(p.getTransport()+p.getUangMakan()));
                    txtLembur.setText(ValidationUtil.formatRupiah(p.getUpahLembur()));
                    txtPotCuti.setText(ValidationUtil.formatRupiah(p.getPotonganCuti()));
                    txtBruto.setText(ValidationUtil.formatRupiah(p.getTotalBruto()));
                    txtPph21.setText(ValidationUtil.formatRupiah(p.getPph21())+"  ("+ptkp+")");
                    txtBpjsKes.setText(ValidationUtil.formatRupiah(p.getBpjsKesKaryawan()));
                    txtBpjsJht.setText(ValidationUtil.formatRupiah(p.getBpjsJhtKaryawan()));
                    txtBpjsJp.setText(ValidationUtil.formatRupiah(p.getBpjsJpKaryawan()));
                    txtBersih.setText(ValidationUtil.formatRupiah(p.getGajiBersih()));
                    // BPJS perusahaan dari total bruto
                    txtBpjsPerusahaan.setText(ValidationUtil.formatRupiah(
                    p.getBpjsKesPerusahaan() + p.getBpjsJhtPerusahaan() +
                    p.getBpjsJpPerusahaan() + p.getBpjsJkk() + p.getBpjsJkm()
                ));
                }catch(Exception e){showErr(e);}
            }
        }.execute();
    }

    private void simpan(){
        Karyawan k=(Karyawan)cbKaryawan.getSelectedItem();
        if(k==null||k.getGolongan()==null)return;

        if(isLocked){
            JOptionPane.showMessageDialog(this,"Periode sudah dikunci.");
            return;
        }

        int b=(int)cbBulan.getSelectedItem(),
            t=(int)spnTahun.getValue();

        boolean doPro=chkProrata.isSelected();

        new SwingWorker<Void,Void>(){

            @Override
            protected Void doInBackground() throws Exception{

                double lem=
                    lemburDAO.getTotalUpahByKaryawanBulan(
                        k.getIdKaryawan(),b,t);

                int alpha=
                    cutiDAO.getTotalAlpha(
                        k.getIdKaryawan(),b,t);

                double pot=
                    (k.getGolongan().getGajiPokok()/25.0)*alpha;

                boolean pro=false;
                int hAkt=0,hBln=0;

                if(doPro&&dcTglMasukBulan.getDate()!=null){

                    java.util.List<LocalDate> lib=
                        hariLiburDAO.getAllTanggal();

                    LocalDate tM=
                        dcTglMasukBulan.getDate()
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();

                    ProRataCalculator.Result pr=
                        ProRataCalculator.hitung(
                            k.getGolongan().getGajiPokok(),
                            tM,
                            YearMonth.of(t,b).atEndOfMonth(),
                            lib
                        );

                    hAkt=pr.hariKerjaAktual;
                    hBln=pr.hariKerjaBulan;
                    pro=true;
                }

                Penggajian p=
                    GajiCalculator.hitung(
                        k,lem,pot,pro,hAkt,hBln);

                p.setPeriodeBulan(b);
                p.setPeriodeTahun(t);
                p.setStatusBayar(
                    (String)cbStatus.getSelectedItem());

                dao.insert(p);

                AuditService.logInsert(
                    "penggajian",
                    k.getIdKaryawan()+"_"+b+"_"+t
                );

                return null;
            }

            @Override
            protected void done(){
                try{
                    get();

                    JOptionPane.showMessageDialog(
                        PenggajianPanel.this,
                        "Gaji berhasil disimpan."
                    );

                    reset();
                    loadTable();

                }catch(Exception e){
                    showErr(e);
                }
            }

        }.execute();
    }

    private void update(){
        if(selected==null){JOptionPane.showMessageDialog(this,"Pilih data dahulu.");return;}
        if(isLocked){JOptionPane.showMessageDialog(this,"Periode dikunci.");return;}
        new SwingWorker<Void,Void>(){
            @Override protected Void doInBackground() throws Exception{
                selected.setStatusBayar((String)cbStatus.getSelectedItem());
                dao.update(selected);AuditService.logUpdate("penggajian",String.valueOf(selected.getIdGaji()));return null;}
            @Override protected void done(){
                try{get();JOptionPane.showMessageDialog(PenggajianPanel.this,"Data diperbarui.");reset();loadTable();}
                catch(Exception e){showErr(e);}
            }
        }.execute();
    }

    private void hapus(){
        if(selected==null||isLocked)return;
        int ok=JOptionPane.showConfirmDialog(this,"Hapus data gaji ini?","Konfirmasi",JOptionPane.YES_NO_OPTION);
        if(ok!=JOptionPane.YES_OPTION)return;
        new SwingWorker<Void,Void>(){
            @Override protected Void doInBackground() throws Exception{
                dao.delete(selected.getIdGaji());AuditService.logDelete("penggajian",String.valueOf(selected.getIdGaji()));return null;}
            @Override protected void done(){
                try{get();reset();loadTable();}catch(Exception e){showErr(e);}
            }
        }.execute();
    }

    private void cetakSlip(){
        if(selected==null){JOptionPane.showMessageDialog(this,"Pilih data gaji dahulu.");return;}
        Karyawan k=(Karyawan)cbKaryawan.getSelectedItem();if(k==null)return;
        JFileChooser fc=new JFileChooser();fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if(fc.showSaveDialog(this)!=JFileChooser.APPROVE_OPTION)return;
        new SwingWorker<java.io.File,Void>(){
            @Override protected java.io.File doInBackground() throws Exception{
                return PDFGenerator.generateSlipGaji(selected,k,fc.getSelectedFile().getAbsolutePath());}
            @Override protected void done(){
                try{java.io.File f=get();AuditService.logExport("Slip gaji "+k.getNama());
                    JOptionPane.showMessageDialog(PenggajianPanel.this,"Slip berhasil:\n"+f.getAbsolutePath());}
                catch(Exception e){showErr(e);}
            }
        }.execute();
    }

    private void lockPeriode(){
        if(selected==null){JOptionPane.showMessageDialog(this,"Pilih data gaji dahulu.");return;}
        int ok=JOptionPane.showConfirmDialog(this,"Kunci periode ini?","Konfirmasi",JOptionPane.YES_NO_OPTION);
        if(ok!=JOptionPane.YES_OPTION)return;
        new SwingWorker<Void,Void>(){
            @Override protected Void doInBackground() throws Exception{
                dao.lock(selected.getIdGaji());
                AuditService.logLock(ValidationUtil.getPeriodeLabel(selected.getPeriodeBulan(),selected.getPeriodeTahun()));
                return null;}
            @Override protected void done(){
                try{get();JOptionPane.showMessageDialog(PenggajianPanel.this,"Periode dikunci.");reset();loadTable();}
                catch(Exception e){showErr(e);}
            }
        }.execute();
    }

    private void reset(){
        selected=null;isLocked=false;lblLock.setText(" ");lblProrataInfo.setText(" ");
        chkProrata.setSelected(false);dcTglMasukBulan.setDate(null);dcTglMasukBulan.setEnabled(false);
        table.clearSelection();setFieldsEnabled(true);
        clearFields();
    }

    private void clearFields() {

    txtGolongan.setText("-");

    txtPokok.setText("");
    txtTjIstri.setText("");
    txtTjAnak.setText("");
    txtTransport.setText("");

    txtLembur.setText("");
    txtPotCuti.setText("");
    txtAlphaInfo.setText("0 hari alpha");

    txtPph21.setText("");
    txtBpjsKes.setText("");
    txtBpjsJht.setText("");
    txtBpjsJp.setText("");

    txtBruto.setText("");
    txtBersih.setText("");
    txtBpjsPerusahaan.setText("");
}
    
    private void onSelectRow(){
        int row=table.getSelectedRow();if(row<0)return;
        int id=(int)tableModel.getValueAt(row,0);
        selected=new Penggajian();selected.setIdGaji(id);
        selected.setPeriodeBulan((int)cbBulan.getSelectedItem());
        selected.setPeriodeTahun((int)spnTahun.getValue());
        isLocked="Terkunci".equals(tableModel.getValueAt(row,8));
        lblLock.setText(isLocked?"Periode ini sudah dikunci.":" ");
        setFieldsEnabled(!isLocked);
    }

    private void setFieldsEnabled(boolean e){
        boolean a=RoleManager.getInstance().isAdmin();
        btnSimpan.setEnabled(e&&a);btnUpdate.setEnabled(e&&a);btnHapus.setEnabled(e&&a);
    }

    private JPanel periodePanel(){
        JPanel p=new JPanel(new FlowLayout(FlowLayout.LEFT,4,0));p.add(cbBulan);p.add(spnTahun);return p;}
    private JTextField ro(String v){JTextField tf=new JTextField(v,18);tf.setEditable(false);tf.setFont(new Font("Segoe UI",Font.PLAIN,11));tf.setBackground(UIManager.getColor("TextField.inactiveBackground"));return tf;}
    private void r(JPanel p,GridBagConstraints gc,int row,String l1,Component c1,String l2,Component c2){
        gc.gridy=row;gc.gridx=0;gc.weightx=0;p.add(new JLabel(l1),gc);
        gc.gridx=1;gc.weightx=1;p.add(c1,gc);
        if(l2!=null&&!l2.isEmpty()){gc.gridx=2;gc.weightx=0;p.add(new JLabel(l2),gc);gc.gridx=3;gc.weightx=1;p.add(c2,gc);}
    }
    private JTable buildTable(DefaultTableModel m){JTable t=new JTable(m);t.setFont(new Font("Segoe UI",Font.PLAIN,11));t.getTableHeader().setFont(new Font("Segoe UI",Font.BOLD,11));t.setRowHeight(24);t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);return t;}
    private void sb(JButton b,Color bg,Color fg){b.setBackground(bg);b.setForeground(fg);b.setFocusPainted(false);b.setBorderPainted(false);}
    private void showErr(Exception e){JOptionPane.showMessageDialog(this,"Error: "+e.getMessage(),"Kesalahan",JOptionPane.ERROR_MESSAGE);}
}
