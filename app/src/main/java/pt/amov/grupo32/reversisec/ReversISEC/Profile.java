package pt.amov.grupo32.reversisec.ReversISEC;


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
