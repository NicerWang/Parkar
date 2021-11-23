package nk.parkar.simulator.hardware;

public interface ControlPanel {
    public String inputLicense(String license);
    public String inputInfo(String tel, Long end, String QR);
}
