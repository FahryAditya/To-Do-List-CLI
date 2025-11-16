import java.sql.*;
import java.util.Scanner;

public class NotesApp {

    private static final String URL = "jdbc:mysql://localhost:3306/notes_app";
    private static final String USER = "root";
    private static final String PASS = "";

    static Connection conn;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Connected to MySQL!");
            menu();
        } catch (Exception e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }

    static void menu() {
        while (true) {
            System.out.println("\n=== NOTES MENU ===");
            System.out.println("1. Lihat Catatan");
            System.out.println("2. Tambah Catatan");
            System.out.println("3. Edit Catatan");
            System.out.println("4. Hapus Catatan");
            System.out.println("5. Cari");
            System.out.println("0. Keluar");
            System.out.print("Pilih: ");

            int pilih = scanner.nextInt();
            scanner.nextLine();

            switch (pilih) {
                case 1 -> listNotes();
                case 2 -> addNote();
                case 3 -> editNote();
                case 4 -> deleteNote();
                case 5 -> searchNote();
                case 0 -> System.exit(0);
                default -> System.out.println("Pilihan tidak valid!");
            }
        }
    }

    static void listNotes() {
        try {
            String q = "SELECT id, title, created_at FROM notes ORDER BY id DESC";
            PreparedStatement st = conn.prepareStatement(q);
            ResultSet rs = st.executeQuery();

            System.out.println("\n=== Daftar Catatan ===");
            while (rs.next()) {
                System.out.println("[" + rs.getInt("id") + "] "
                        + rs.getString("title") + " - "
                        + rs.getTimestamp("created_at"));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void addNote() {
        try {
            System.out.print("Judul: ");
            String title = scanner.nextLine();

            System.out.print("Isi: ");
            String content = scanner.nextLine();

            String q = "INSERT INTO notes (title, content) VALUES (?, ?)";
            PreparedStatement st = conn.prepareStatement(q);
            st.setString(1, title);
            st.setString(2, content);
            st.executeUpdate();

            System.out.println("Catatan berhasil ditambahkan!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void editNote() {
        try {
            System.out.print("ID catatan: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Judul baru: ");
            String title = scanner.nextLine();

            System.out.print("Isi baru: ");
            String content = scanner.nextLine();

            String q = "UPDATE notes SET title=?, content=? WHERE id=?";
            PreparedStatement st = conn.prepareStatement(q);
            st.setString(1, title);
            st.setString(2, content);
            st.setInt(3, id);
            st.executeUpdate();

            System.out.println("Catatan berhasil diupdate!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void deleteNote() {
        try {
            System.out.print("ID catatan: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            String q = "DELETE FROM notes WHERE id=?";
            PreparedStatement st = conn.prepareStatement(q);
            st.setInt(1, id);
            st.executeUpdate();

            System.out.println("Catatan berhasil dihapus!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void searchNote() {
        try {
            System.out.print("Kata kunci: ");
            String keyword = scanner.nextLine();

            String q = "SELECT id, title FROM notes WHERE title LIKE ? OR content LIKE ?";
            PreparedStatement st = conn.prepareStatement(q);
            st.setString(1, "%" + keyword + "%");
            st.setString(2, "%" + keyword + "%");

            ResultSet rs = st.executeQuery();

            System.out.println("\n=== Hasil Pencarian ===");
            while (rs.next()) {
                System.out.println("[" + rs.getInt("id") + "] " + rs.getString("title"));
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
