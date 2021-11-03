package nk.parkar.service;


public interface AdminService {
    boolean login(String name, String pwd);
    boolean updatePwd(String name, String pwd);
}
