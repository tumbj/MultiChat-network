//นายธนดล คงสกุล 5910450298 sec:200
//นายนักกระวี คืนคลีบ 5910451073 sec:200
public interface OutputCallback {
    void onConnected(Communicate commu);
    void onDisconected(Communicate commu);
    void onReceived(Communicate channel, String msg);
}