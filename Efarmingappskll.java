// Filename: Efarmingappskll.java

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

class Product {
    String name;
    double price;
    int quantity;
    String unit;
    String sellerName;

    Product(String name, double price, int quantity, String unit, String sellerName) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.unit = unit;
        this.sellerName = sellerName;
    }

    public String toString() {
        return "Product: " + name + " | Price: ₹" + price + " | Quantity: " + quantity + " " + unit + " | Seller: " + sellerName;
    }
}

class User {
    String name, pass, role, address, phone;

    User(String name, String pass, String role, String address, String phone) {
        this.name = name;
        this.pass = pass;
        this.role = role;
        this.address = address;
        this.phone = phone;
    }

    public String toString() {
        return "Name: " + name + " | Role: " + role + " | Phone: " + phone + " | Address: " + address;
    }
}

public class Efarmingappskll {
    static ArrayList<Product> products = new ArrayList<>();
    static ArrayList<User> users = new ArrayList<>();

    public static void main(String[] args) {
        loadProductsFromFile();
        loadUsersFromFile();
        showMainWindow();
    }

    public static void showMainWindow() {
        JFrame frame = new JFrame("🌿 E-Farming Home");
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JLabel title = new JLabel("🌾 Welcome to E-FARMING PLATFORM 🌾", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI Emoji", Font.BOLD, 28));
        title.setForeground(new Color(34, 139, 34));
        title.setBorder(BorderFactory.createEmptyBorder(30, 10, 20, 10));
        frame.add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        centerPanel.setOpaque(false);

        JButton adminBtn = new JButton("🔐 Admin Login");
        JButton sellerBtn = new JButton("👨‍🌾 Seller Login");
        JButton buyerBtn = new JButton("🛒 Buyer Login");
        JButton aboutBtn = new JButton("ℹ About");
        JButton contactBtn = new JButton("📞 Contact");
        JButton exitBtn = new JButton("❌ Exit");

        Font btnFont = new Font("Segoe UI Emoji", Font.PLAIN, 18);
        JButton[] buttons = {adminBtn, sellerBtn, buyerBtn, aboutBtn, contactBtn, exitBtn};
        for (JButton btn : buttons) {
            btn.setFont(btnFont);
            btn.setBackground(Color.WHITE);
            btn.setFocusPainted(false);
            centerPanel.add(btn);
        }

        adminBtn.addActionListener(e -> { frame.dispose(); showLogin("Admin"); });
        sellerBtn.addActionListener(e -> { frame.dispose(); showLogin("Seller"); });
        buyerBtn.addActionListener(e -> { frame.dispose(); showLogin("Buyer"); });

        aboutBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame,
                "🌿 E-Farming connects farmers and buyers.\nBuy and sell fresh produce directly with ease."));

        contactBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame,
                "📧 Email: mirapamanikanta@gmail.com\n📱 Phone: 7386292303"));

        exitBtn.addActionListener(e -> System.exit(0));

        JPanel paddingPanel = new JPanel(new GridBagLayout());
        paddingPanel.setOpaque(false);
        paddingPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 50));
        paddingPanel.add(centerPanel);

        frame.add(paddingPanel, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void showLogin(String role) {
        JFrame frame = new JFrame(role + " Login/Register");
        frame.setSize(350, 350);
        frame.setLayout(new GridLayout(8, 2));

        JTextField name = new JTextField();
        JPasswordField pass = new JPasswordField();
        JTextField phone = new JTextField();
        JTextField address = new JTextField();

        JButton login = new JButton("Login");
        JButton register = new JButton("Register");
        JButton back = new JButton("Back");

        frame.add(new JLabel(role + " Name:")); frame.add(name);
        frame.add(new JLabel("Password:")); frame.add(pass);

        if (!role.equals("Admin")) {
            frame.add(new JLabel("Phone (10 digits):")); frame.add(phone);
            frame.add(new JLabel("Address:")); frame.add(address);
        } else {
            phone.setText("NA");
            address.setText("NA");
        }

        frame.add(login); frame.add(register);
        frame.add(back);

        login.addActionListener(e -> {
            String n = name.getText();
            String p = new String(pass.getPassword());
            for (User u : users) {
                if (u.name.equals(n) && u.pass.equals(p) && u.role.equals(role)) {
                    frame.dispose();
                    if (role.equals("Admin")) showAdminPanel();
                    else if (role.equals("Seller")) showSellerPanel(n);
                    else showBuyerPanel(n);
                    return;
                }
            }
            JOptionPane.showMessageDialog(frame, "❌ Invalid credentials");
        });

        register.addActionListener(e -> {
            String n = name.getText();
            String p = new String(pass.getPassword());
            String ph = phone.getText();
            String ad = address.getText();

            if (n.isEmpty() || p.isEmpty() || (!role.equals("Admin") && (ph.isEmpty() || ad.isEmpty()))) {
                JOptionPane.showMessageDialog(frame, "⚠ All fields are required.");
                return;
            }
            if (!role.equals("Admin") && !ph.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(frame, "⚠ Phone number must be 10 digits.");
                return;
            }

            for (User u : users) {
                if (u.name.equals(n) && u.role.equals(role)) {
                    JOptionPane.showMessageDialog(frame, "⚠ User already exists.");
                    return;
                }
            }

            users.add(new User(n, p, role, ad, ph));
            saveUsersToFile();
            JOptionPane.showMessageDialog(frame, "✅ Registered. Now login.");
        });

        back.addActionListener(e -> {
            frame.dispose();
            showMainWindow();
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void showSellerPanel(String sellerName) {
        JFrame frame = new JFrame("Seller Panel - " + sellerName);
        frame.setSize(700, 500);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField qtyField = new JTextField();
        JTextField unitField = new JTextField();

        inputPanel.add(new JLabel("Product Name:")); inputPanel.add(nameField);
        inputPanel.add(new JLabel("Price:")); inputPanel.add(priceField);
        inputPanel.add(new JLabel("Quantity:")); inputPanel.add(qtyField);
        inputPanel.add(new JLabel("Unit (kg/L):")); inputPanel.add(unitField);

        JButton add = new JButton("Add Product");
        JButton back = new JButton("Back");
        inputPanel.add(add); inputPanel.add(back);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);

        JPanel deletePanel = new JPanel();
        JTextField delIndexField = new JTextField(5);
        JButton deleteBtn = new JButton("Delete Product by No");
        deletePanel.add(new JLabel("Enter Product No:"));
        deletePanel.add(delIndexField);
        deletePanel.add(deleteBtn);

        add.addActionListener(e -> {
            try {
                String pname = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                int qty = Integer.parseInt(qtyField.getText());
                String unit = unitField.getText();
                Product p = new Product(pname, price, qty, unit, sellerName);
                products.add(p);
                saveProductsToFile();
                updateSellerProducts(area, sellerName);
                area.append("\n✅ Added: " + p);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "❌ Invalid Input");
            }
        });

        deleteBtn.addActionListener(e -> {
            try {
                int index = Integer.parseInt(delIndexField.getText()) - 1;
                if (index >= 0 && index < products.size()) {
                    Product p = products.get(index);
                    if (p.sellerName.equals(sellerName)) {
                        products.remove(index);
                        saveProductsToFile();
                        updateSellerProducts(area, sellerName);
                        JOptionPane.showMessageDialog(frame, "🗑️ Product deleted.");
                    } else {
                        JOptionPane.showMessageDialog(frame, "⚠ You can only delete your own products.");
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "❌ Invalid input.");
            }
        });

        back.addActionListener(e -> {
            frame.dispose();
            showMainWindow();
        });

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(scroll, BorderLayout.CENTER);
        frame.add(deletePanel, BorderLayout.SOUTH);

        updateSellerProducts(area, sellerName);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void updateSellerProducts(JTextArea area, String sellerName) {
        StringBuilder sb = new StringBuilder("📦 Your Products:\n\n");
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).sellerName.equals(sellerName)) {
                sb.append((i + 1)).append(". ").append(products.get(i)).append("\n");
            }
        }
        area.setText(sb.toString());
    }

    public static void showBuyerPanel(String buyerName) {
        JFrame frame = new JFrame("Buyer Panel - " + buyerName);
        frame.setSize(650, 500);
        frame.setLayout(new BorderLayout());

        JTextArea area = new JTextArea();
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);

        JTextField index = new JTextField();
        JTextField qty = new JTextField();
        JTextField search = new JTextField();

        JButton buy = new JButton("Buy");
        JButton back = new JButton("Back");
        JButton find = new JButton("Search");

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Search Name:")); panel.add(search);
        panel.add(find); panel.add(new JLabel(""));
        panel.add(new JLabel("Product No:")); panel.add(index);
        panel.add(new JLabel("Quantity:")); panel.add(qty);

        JPanel bottom = new JPanel();
        bottom.add(buy); bottom.add(back);

        updateProductList(area);

        find.addActionListener(e -> {
            String key = search.getText().toLowerCase();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).name.toLowerCase().contains(key)) {
                    sb.append((i + 1)).append(". ").append(products.get(i)).append("\n");
                }
            }
            if (sb.isEmpty()) sb.append("No products found.");
            area.setText(sb.toString());
        });

        buy.addActionListener(e -> {
            try {
                int i = Integer.parseInt(index.getText()) - 1;
                int q = Integer.parseInt(qty.getText());
                if (i >= 0 && i < products.size() && q > 0) {
                    Product p = products.get(i);
                    if (p.quantity >= q) {
                        p.quantity -= q;
                        saveProductsToFile();
                        updateProductList(area);
                        generateBill(buyerName, p.name, p.price, q, p.unit);
                        JOptionPane.showMessageDialog(frame, "✅ Purchase done. Bill saved.");
                    } else JOptionPane.showMessageDialog(frame, "❌ Not enough stock");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "❌ Invalid input");
            }
        });

        back.addActionListener(e -> {
            frame.dispose();
            showMainWindow();
        });

        frame.add(scroll, BorderLayout.CENTER);
        frame.add(panel, BorderLayout.NORTH);
        frame.add(bottom, BorderLayout.SOUTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void showAdminPanel() {
        JFrame frame = new JFrame("Admin Panel");
        frame.setSize(650, 500);
        frame.setLayout(new BorderLayout());

        JTextArea area = new JTextArea();
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);

        JButton delete = new JButton("Delete Product");
        JButton back = new JButton("Back");
        JTextField index = new JTextField(5);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Enter Product No to Delete:"));
        panel.add(index);
        panel.add(delete); panel.add(back);

        updateProductList(area);

        delete.addActionListener(e -> {
            try {
                int i = Integer.parseInt(index.getText()) - 1;
                if (i >= 0 && i < products.size()) {
                    products.remove(i);
                    saveProductsToFile();
                    updateProductList(area);
                }
            } catch (Exception ignored) {}
        });

        back.addActionListener(e -> {
            frame.dispose();
            showMainWindow();
        });

        frame.add(scroll, BorderLayout.CENTER);
        frame.add(panel, BorderLayout.SOUTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void updateProductList(JTextArea area) {
        StringBuilder sb = new StringBuilder("Product List:\n\n");
        for (int i = 0; i < products.size(); i++) {
            sb.append((i + 1)).append(". ").append(products.get(i)).append("\n");
        }
        if (products.isEmpty()) sb.append("No products available.");
        area.setText(sb.toString());
    }

    public static void generateBill(String name, String pname, double price, int qty, String unit) {
        try (PrintWriter pw = new PrintWriter("bill_" + name + ".txt")) {
            pw.println("E-Farming Receipt\n====================");
            pw.println("Buyer: " + name);
            pw.println("Product: " + pname);
            pw.println("Quantity: " + qty + " " + unit);
            pw.println("Price per Unit: ₹" + price);
            pw.println("Total: ₹" + (price * qty));
            pw.println("Thank you for shopping!");
        } catch (IOException ignored) {}
    }

    public static void loadProductsFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("products.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(";");
                if (p.length == 5)
                    products.add(new Product(p[0], Double.parseDouble(p[1]), Integer.parseInt(p[2]), p[3], p[4]));
            }
        } catch (IOException ignored) {}
    }

    public static void saveProductsToFile() {
        try (PrintWriter pw = new PrintWriter("products.txt")) {
            for (Product p : products)
                pw.println(p.name + ";" + p.price + ";" + p.quantity + ";" + p.unit + ";" + p.sellerName);
        } catch (IOException ignored) {}
    }

    public static void loadUsersFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(";");
                if (p.length == 5)
                    users.add(new User(p[0], p[1], p[2], p[3], p[4]));
            }
        } catch (IOException ignored) {}
    }

    public static void saveUsersToFile() {
        try (PrintWriter pw = new PrintWriter("users.txt")) {
            for (User u : users)
                pw.println(u.name + ";" + u.pass + ";" + u.role + ";" + u.address + ";" + u.phone);
        } catch (IOException ignored) {}
    }
}
