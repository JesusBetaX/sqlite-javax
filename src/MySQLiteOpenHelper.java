import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.database.sqlite.SQLiteDatabase;
import javax.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

  private static final String DATABASE_NAME  = "tests.db";
  private static final int DATABASE_VERSION  = 1;
  
  public MySQLiteOpenHelper() {
    super(DATABASE_NAME, DATABASE_VERSION);
  }

  @Override public void onCreate(SQLiteDatabase db) throws SQLException {
    System.out.println("sqlitejdbc.MySQLiteOpenHelper.onCreate()");
    // SQL statement for creating a new table
    String sql = "CREATE TABLE IF NOT EXISTS warehouses (\n"
            + "	id integer PRIMARY KEY,\n"
            + "	name text NOT NULL,\n"
            + "	capacity real\n"
            + ");";
    db.execSQL(sql);
  }

  /**
   * select all rows in the warehouses table
   *
   * @throws java.sql.SQLException
   */
  public void selectAll() throws SQLException {
    String sql = "SELECT id, name, capacity FROM warehouses";

    try (SQLiteDatabase db = this.getReadableDatabase();
            Statement stmt = db.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

      // loop through the result set
      while (rs.next()) {
        System.out.println(rs.getInt("id") + "\t"
                + rs.getString("name") + "\t"
                + rs.getDouble("capacity"));
      }
    }
  }

  /**
   * Insert a new row into the warehouses table
   *
   * @param name
   * @param capacity
   * @return
   * @throws java.sql.SQLException
   */
  public int insert(String name, double capacity) throws SQLException {
    String sql = "INSERT INTO warehouses(name,capacity) VALUES(?,?)";
    try (SQLiteDatabase db = this.getWritableDatabase();
            PreparedStatement pstmt = db.compileStatement(sql)) {

      pstmt.setString(1, name);
      pstmt.setDouble(2, capacity);
      return pstmt.executeUpdate();
    }
  }

  /**
   * Update data of a warehouse specified by the id
   *
   * @param id
   * @param name name of the warehouse
   * @param capacity capacity of the warehouse
   * @return
   * @throws java.sql.SQLException
   */
  public int update(long id, String name, double capacity) throws SQLException {
    String sql = "UPDATE warehouses SET name = ? , "
            + "capacity = ? "
            + "WHERE id = ?";

    try (SQLiteDatabase db = this.getWritableDatabase();
            PreparedStatement pstmt = db.compileStatement(sql)) {

      // set the corresponding param
      pstmt.setString(1, name);
      pstmt.setDouble(2, capacity);
      pstmt.setLong(3, id);
      // update 
      return pstmt.executeUpdate();
    }
  }

  /**
   * Delete a warehouse specified by the id
   *
   * @param id
   * @return 
   * @throws java.sql.SQLException
   */
  public int delete(long id) throws SQLException {
    String sql = "DELETE FROM warehouses WHERE id = ?";

    try (SQLiteDatabase db = this.getWritableDatabase();
            PreparedStatement pstmt = db.compileStatement(sql)) {

      // set the corresponding param
      pstmt.setLong(1, id);
      // execute the delete statement
      return pstmt.executeUpdate();
    } 
  }

  public static void main(String[] args) throws SQLException {
    MySQLiteOpenHelper dbHelper = new MySQLiteOpenHelper();
    //dbHelper.insert("Almacen 1", 93);
    dbHelper.selectAll();
  }
}