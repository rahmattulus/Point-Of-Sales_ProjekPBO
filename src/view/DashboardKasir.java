/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import model.ItemTransaksi;
import model.Keranjang;
import model.Transaksi;
import repository.DataAccessException;
import service.AuthService;
import service.Sesi;
import service.TransaksiService;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

/**
 * Form transaksi kasir (penjualan barang, keranjang belanja, pembayaran).
 * Menggunakan {@link Keranjang} dan {@link TransaksiService} untuk proses bisnis.
 *
 * @author POS-KasirJAVA Refactor
 */
public class DashboardKasir extends javax.swing.JFrame {

    /** Keranjang belanja transaksi aktif. */
    private final Keranjang keranjang = new Keranjang();

    /** Service untuk logika transaksi dan pembuatan no faktur. */
    private final TransaksiService txService = new TransaksiService();

    /** Service otentikasi untuk logout. */
    private final AuthService authService = new AuthService();

    /**
     * Creates new form DashboardKasir.
     */
    public DashboardKasir() {
        initComponents();
        setTitle("Menu Kasir");
        setLocationRelativeTo(null);

        String usernameKasir = Sesi.isLogin() ? Sesi.getPenggunaAktif().getUsername() : "Kasir";
        txtkasir.setText("Kasir : " + usernameKasir);
        txtNoTransaksi.setText(txService.buatNomorFaktur());

        // Timer untuk memperbarui jam/tanggal setiap detik
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtJam.setText(jam());
            }
        });
        timer.start();
        txtJam.setText(jam());
    }

    /**
     * Format string jam dan tanggal saat ini.
     *
     * @return string format HTML untuk JLabel txtJam
     */
    public String jam() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        return "<html>" + currentDateTime.format(timeFormatter) + "<br>" + currentDateTime.format(dateFormatter) + "</html>";
    }

    /** Mengosongkan form input dan total. */
    public void clear() {
        txtKodeBarang.setText("");
        totalAtas.setText("Rp. 0");
        txtBayar.setText("");
        txtKembali.setText("");
    }

    /**
     * Memperbarui tampilan JTable dan total harga dari objek {@link Keranjang}.
     */
    private void refreshTableKeranjang() {
        DefaultTableModel model = (DefaultTableModel) tbls.getModel();
        model.setRowCount(0);
        for (ItemTransaksi item : keranjang.getItems()) {
            model.addRow(new Object[]{
                item.getBarang().getNama(),
                item.getHargaSatuan(),
                item.getQty(),
                item.getSubtotal()
            });
        }
        totalAtas.setText("Rp. " + keranjang.hitungTotal());
    }

    /**
     * Memperbarui kuantitas item pada baris yang dipilih di tabel.
     *
     * @param updateRow indeks baris tabel yang diedit
     */
    private void updateRowQty(int updateRow) {
        if (updateRow < 0 || updateRow >= keranjang.getItems().size()) {
            return;
        }
        try {
            int newQty = Integer.parseInt(tbls.getValueAt(updateRow, 2).toString().trim());
            ItemTransaksi item = keranjang.getItems().get(updateRow);
            String kode = item.getBarang().getKode();
            keranjang.ubahQty(kode, newQty);
            refreshTableKeranjang();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "QTY harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
            refreshTableKeranjang();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Peringatan", JOptionPane.WARNING_MESSAGE);
            refreshTableKeranjang();
        }
    }

    /**
     * Menghitung nilai uang kembalian berdasarkan input bayar dan total belanja.
     */
    private void hitungKembalian() {
        try {
            int total = keranjang.hitungTotal();
            String bayarString = txtBayar.getText().replace("Rp. ", "").replace(".", "").trim();
            if (bayarString.isEmpty()) {
                return;
            }
            int bayar = Integer.parseInt(bayarString);
            int kembalian = bayar - total;
            txtKembali.setText(String.valueOf(kembalian));
        } catch (NumberFormatException e) {
            txtKembali.setText("0");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton3 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        totalAtas = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbls = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        txtBayar = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtKembali = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtKodeBarang = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtNoTransaksi = new javax.swing.JTextField();
        txtJam = new javax.swing.JLabel();
        txtkasir = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        jButton3.setText("jButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 153, 255));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 153, 255));
        jLabel4.setText("DASBOARD KASIR");

        jButton2.setBackground(new java.awt.Color(0, 153, 255));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("List Barang");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(204, 0, 0));
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setText("<- Log-Out");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel4))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
                            .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(156, 156, 156)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton8)
                .addGap(36, 36, 36))
        );

        totalAtas.setBackground(new java.awt.Color(255, 255, 255));
        totalAtas.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        totalAtas.setForeground(new java.awt.Color(255, 255, 255));
        totalAtas.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalAtas.setText("Rp. 0");

        tbls.setBackground(new java.awt.Color(255, 255, 255));
        tbls.setForeground(new java.awt.Color(0, 0, 0));
        tbls.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nama", "Harga", "QTY", "Jumlah"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbls.setSelectionBackground(new java.awt.Color(204, 204, 204));
        tbls.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tbls.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblsMouseClicked(evt);
            }
        });
        tbls.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblsKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblsKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tblsKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(tbls);
        if (tbls.getColumnModel().getColumnCount() > 0) {
            tbls.getColumnModel().getColumn(0).setResizable(false);
            tbls.getColumnModel().getColumn(0).setHeaderValue("Nama");
            tbls.getColumnModel().getColumn(1).setResizable(false);
            tbls.getColumnModel().getColumn(1).setHeaderValue("Harga");
            tbls.getColumnModel().getColumn(2).setResizable(false);
            tbls.getColumnModel().getColumn(2).setHeaderValue("QTY");
            tbls.getColumnModel().getColumn(3).setResizable(false);
            tbls.getColumnModel().getColumn(3).setHeaderValue("Jumlah");
        }

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Bayar");

        txtBayar.setBackground(new java.awt.Color(255, 255, 255));
        txtBayar.setForeground(new java.awt.Color(0, 0, 0));
        txtBayar.setMinimumSize(new java.awt.Dimension(100, 26));
        txtBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBayarActionPerformed(evt);
            }
        });
        txtBayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBayarKeyPressed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Kembali");

        txtKembali.setBackground(new java.awt.Color(255, 255, 255));
        txtKembali.setForeground(new java.awt.Color(0, 0, 0));
        txtKembali.setMinimumSize(new java.awt.Dimension(100, 26));
        txtKembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKembaliActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("TRANSAKSI");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("No. Faktur");

        txtKodeBarang.setBackground(new java.awt.Color(255, 255, 255));
        txtKodeBarang.setForeground(new java.awt.Color(0, 0, 0));
        txtKodeBarang.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtKodeBarang.setCaretColor(new java.awt.Color(0, 0, 0));
        txtKodeBarang.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        txtKodeBarang.setMinimumSize(new java.awt.Dimension(68, 26));
        txtKodeBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKodeBarangActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Kode Barang");

        txtNoTransaksi.setEditable(false);
        txtNoTransaksi.setBackground(new java.awt.Color(255, 255, 255));
        txtNoTransaksi.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        txtNoTransaksi.setForeground(new java.awt.Color(0, 0, 0));
        txtNoTransaksi.setActionCommand("<Not Set>");
        txtNoTransaksi.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtNoTransaksi.setCaretColor(new java.awt.Color(255, 255, 255));
        txtNoTransaksi.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        txtNoTransaksi.setMinimumSize(new java.awt.Dimension(68, 26));
        txtNoTransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNoTransaksiActionPerformed(evt);
            }
        });

        txtJam.setFont(new java.awt.Font("ArianLT-Demi", 1, 14)); // NOI18N
        txtJam.setForeground(new java.awt.Color(255, 255, 255));
        txtJam.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txtJam.setText("Date Time");

        txtkasir.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtkasir.setForeground(new java.awt.Color(255, 255, 255));
        txtkasir.setText("Nama Petugas Kasir");

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 255, 0));
        jButton1.setText("BAYAR");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGap(41, 41, 41)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(txtNoTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtKodeBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(totalAtas, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(27, 27, 27))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtJam, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(415, 415, 415)
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtkasir, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel6)
                                            .addComponent(jLabel5))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtKembali, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(15, 15, 15)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNoTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtKodeBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(totalAtas)))
                .addGap(14, 14, 14)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtkasir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtJam, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtBayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtKembali, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        authService.logout();
        new MenuUtama().setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        new ListBarang().setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void txtBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBayarActionPerformed
        hitungKembalian();
    }//GEN-LAST:event_txtBayarActionPerformed

    private void txtKembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKembaliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKembaliActionPerformed

    private void txtKodeBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKodeBarangActionPerformed
        String productId = txtKodeBarang.getText().trim();
        if (!productId.isEmpty()) {
            boolean success = txService.tambahKeKeranjang(keranjang, productId);
            if (success) {
                refreshTableKeranjang();
            } else {
                JOptionPane.showMessageDialog(this, "Barang tidak ditemukan atau stok tidak mencukupi!",
                        "Peringatan", JOptionPane.WARNING_MESSAGE);
            }
        }
        txtKodeBarang.setText("");
        txtKodeBarang.requestFocusInWindow();
    }//GEN-LAST:event_txtKodeBarangActionPerformed

    private void txtNoTransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNoTransaksiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoTransaksiActionPerformed

    private void tblsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblsKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tblsKeyReleased

    private void tblsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblsKeyPressed
        int updateRow = tbls.getSelectedRow();
        if (updateRow != -1 && evt.getKeyCode() == KeyEvent.VK_ENTER) {
            updateRowQty(updateRow);
        }
    }//GEN-LAST:event_tblsKeyPressed

    private void tblsKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblsKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_tblsKeyTyped

    private void tblsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblsMouseClicked
        if (tbls.isEditing()) {
            tbls.getCellEditor().stopCellEditing();
        }
        int updateRow = tbls.getSelectedRow();
        if (updateRow != -1) {
            updateRowQty(updateRow);
        }
    }//GEN-LAST:event_tblsMouseClicked

    private void txtBayarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBayarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            hitungKembalian();
        }
    }//GEN-LAST:event_txtBayarKeyPressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (keranjang.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keranjang belanja masih kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String bayarString = txtBayar.getText().replace("Rp. ", "").replace(".", "").trim();
        if (bayarString.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan nominal pembayaran!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            txtBayar.requestFocusInWindow();
            return;
        }

        try {
            int bayar = Integer.parseInt(bayarString);
            Transaksi t = txService.bayar(keranjang, bayar);
            int kembalian = t.hitungKembalian(bayar);
            txtKembali.setText(String.valueOf(kembalian));

            String waktu = txtJam.getText().replace("<html>", "").replace("<br>", " | ").replace("</html>", "").trim();
            JOptionPane.showMessageDialog(this,
                    "<html>Transaksi Berhasil<br>No. Faktur: " + t.getNoTransaksi()
                    + "<br>" + waktu
                    + "<br><b>KEMBALIAN : Rp. " + kembalian + "</b></html>",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);

            keranjang.kosongkan();
            clear();
            refreshTableKeranjang();
            txtNoTransaksi.setText(txService.buatNomorFaktur());

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Nominal pembayaran harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Peringatan", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalStateException | DataAccessException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error Transaksi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DashboardKasir.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DashboardKasir.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DashboardKasir.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DashboardKasir.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DashboardKasir().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbls;
    private javax.swing.JLabel totalAtas;
    private javax.swing.JTextField txtBayar;
    private javax.swing.JLabel txtJam;
    private javax.swing.JTextField txtKembali;
    private javax.swing.JTextField txtKodeBarang;
    private javax.swing.JTextField txtNoTransaksi;
    private javax.swing.JLabel txtkasir;
    // End of variables declaration//GEN-END:variables
}
