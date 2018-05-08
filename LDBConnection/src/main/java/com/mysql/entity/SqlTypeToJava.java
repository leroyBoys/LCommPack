package com.mysql.entity;

import java.math.BigDecimal;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by leroy:656515489@qq.com
 * 2018/5/8.
 */
public class SqlTypeToJava {
    public static final Map<Class,SqlTypeToJava> sqlTypsMap = new HashMap<>();
    static {
        sqlTypsMap.put(String.class,new SqlTypeToJava());
        sqlTypsMap.put(int.class,new IntToJava());
        sqlTypsMap.put(Integer.class,new IntToJava());
        sqlTypsMap.put(float.class,new FloatToJava());
        sqlTypsMap.put(Float.class,new FloatToJava());
        sqlTypsMap.put(double.class,new FloatToJava());
        sqlTypsMap.put(Double.class,new FloatToJava());
        sqlTypsMap.put(BigDecimal.class,new BigDecimalToJava());
        sqlTypsMap.put(byte.class,new ByteStreamToJava());
        sqlTypsMap.put(Byte.class,new ByteStreamToJava());
        sqlTypsMap.put(byte[].class,new BytesStreamToJava());
        sqlTypsMap.put(Blob.class,new BlobToJava());
        sqlTypsMap.put(Boolean.class,new BooleanToJava());
        sqlTypsMap.put(boolean.class,new BooleanToJava());
        sqlTypsMap.put(java.io.Reader.class,new CharacterStreamToJava());
        sqlTypsMap.put(Clob.class,new ClobStreamToJava());
        sqlTypsMap.put(Long.class,new LongToJava());
        sqlTypsMap.put(long.class,new LongToJava());
        sqlTypsMap.put(java.sql.Date.class,new DateToJava());
        sqlTypsMap.put(java.util.Date.class,new UDateToJava());
        sqlTypsMap.put(Timestamp.class,new TimestampToJava());
        sqlTypsMap.put(java.net.URL.class,new URLToJava());
    }

    public Object get(ResultSet rs,String colum) throws SQLException {
        return rs.getString(colum);
    }

    public static class IntToJava extends SqlTypeToJava{
        @Override
        public Object get(ResultSet rs, String colum) throws SQLException {
            return rs.getInt(colum);
        }
    }

    public static class FloatToJava extends SqlTypeToJava{
        @Override
        public Object get(ResultSet rs, String colum) throws SQLException {
            return rs.getFloat(colum);
        }
    }

    public static class DoubleToJava extends SqlTypeToJava{
        @Override
        public Object get(ResultSet rs, String colum) throws SQLException {
            return rs.getDouble(colum);
        }
    }

    public static class BigDecimalToJava extends SqlTypeToJava{
        @Override
        public Object get(ResultSet rs, String colum) throws SQLException {
            return rs.getBigDecimal(colum);
        }
    }

    public static class ByteStreamToJava extends SqlTypeToJava{
        @Override
        public Object get(ResultSet rs, String colum) throws SQLException {
            return rs.getByte(colum);
        }
    }

    public static class BytesStreamToJava extends SqlTypeToJava{
        @Override
        public Object get(ResultSet rs, String colum) throws SQLException {
            return rs.getBytes(colum);
        }
    }

    public static class UDateToJava extends SqlTypeToJava{
        @Override
        public Object get(ResultSet rs, String colum) throws SQLException {
            java.sql.Timestamp timestamp = rs.getTimestamp(colum);
            if(timestamp == null){
                return null;
            }
            return new java.util.Date(timestamp.getTime());
        }
    }

    public static class DateToJava extends SqlTypeToJava{
        @Override
        public Object get(ResultSet rs, String colum) throws SQLException {
            return rs.getDate(colum);
        }
    }

    public static class TimestampToJava extends SqlTypeToJava{
        @Override
        public Object get(ResultSet rs, String colum) throws SQLException {
            return rs.getTimestamp(colum);
        }
    }

    public static class LongToJava extends SqlTypeToJava{
        @Override
        public Object get(ResultSet rs, String colum) throws SQLException {
            return rs.getLong(colum);
        }
    }

    public static class URLToJava extends SqlTypeToJava{
        @Override
        public Object get(ResultSet rs, String colum) throws SQLException {
            return rs.getURL(colum);
        }
    }

    public static class ClobStreamToJava extends SqlTypeToJava{
        @Override
        public Object get(ResultSet rs, String colum) throws SQLException {
            return rs.getClob(colum);
        }
    }

    public static class CharacterStreamToJava extends SqlTypeToJava{
        @Override
        public Object get(ResultSet rs, String colum) throws SQLException {
            return rs.getCharacterStream(colum);
        }
    }

    public static class BooleanToJava extends SqlTypeToJava{
        @Override
        public Object get(ResultSet rs, String colum) throws SQLException {
            return rs.getBoolean(colum);
        }
    }

    public static class BlobToJava extends SqlTypeToJava{
        @Override
        public Object get(ResultSet rs, String colum) throws SQLException {
            return rs.getBlob(colum);
        }
    }

    public static class BinaryStreamToJava extends SqlTypeToJava{
        @Override
        public Object get(ResultSet rs, String colum) throws SQLException {
            return rs.getBinaryStream(colum);
        }
    }

    public static class AsciiStreamToJava extends SqlTypeToJava{
        @Override
        public Object get(ResultSet rs, String colum) throws SQLException {
            return rs.getAsciiStream(colum);
        }
    }

    public static class ArrayToJava extends SqlTypeToJava{
        @Override
        public Object get(ResultSet rs, String colum) throws SQLException {
            return rs.getArray(colum);
        }
    }
}
