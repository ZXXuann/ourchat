package com.demo.wechat.entity.config;

import com.demo.wechat.utils.StringTools;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author ZXX
 * @Date 2024/11/18 17:28
 */
@Component("appConfig")
public class AppConfig {
    @Value("${ws.port:}")
    private Integer wsPort;
    @Value("${project.folder:}")
    private String projectFolder;
    @Value("${admin.emails:}")
    private String adminEmails;
    @Value("${app.runCheckCode}")
    private Boolean runCheckCode;

    public Boolean getRunCheckCode() {
        return runCheckCode;
    }

    public void setRunCheckCode(Boolean runCheckCode) {
        this.runCheckCode = runCheckCode;
    }

    public Integer getWsPort() {
        return wsPort;
    }

    public void setWsPort(Integer wsPort) {
        this.wsPort = wsPort;
    }

    public String getProjectFolder() {
        return projectFolder;
    }

    public void setProjectFolder(String projectFolder) {
        if(StringTools.isEmpty(projectFolder)&&!projectFolder.endsWith("/")){
            projectFolder=projectFolder+"/";
        }
        this.projectFolder = projectFolder;
    }

    public String getAdminEmails() {
        return adminEmails;
    }

    public void setAdminEmails(String adminEmails) {
        this.adminEmails = adminEmails;
    }
}
