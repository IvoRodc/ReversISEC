package pt.amov.grupo32.reversisec.ReversISEC.SharedPreferences;


public class Profile{

    private String nickname;
    private byte[] fotografia;

    public Profile(){
        nickname = null;
        fotografia = null;
    }

    public Profile(String name){
        nickname = name;
        fotografia = null;
    }

    public Profile(String name, byte[] foto){
        nickname = name;
        fotografia = foto;
    }

    public String getNickname(){
        return nickname;
    }
    public void setNickname(String name){
        nickname = name;
    }

    public byte[] getFotografia(){
        return fotografia;
    }
    public void setFotografia(byte[] foto){
        fotografia = foto;
    }


}
