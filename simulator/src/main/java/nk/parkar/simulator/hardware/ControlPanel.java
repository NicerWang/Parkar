package nk.parkar.simulator.hardware;

import org.springframework.stereotype.Component;


public interface ControlPanel {
    void InputUserTel(String tel);
    void Notify();
}
