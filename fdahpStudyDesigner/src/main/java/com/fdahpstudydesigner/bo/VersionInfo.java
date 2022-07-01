package com.fdahpstudydesigner.bo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "version_info")
public class VersionInfo implements Serializable {

    private static final long serialVersionUID = 135353554543L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "version_info_id")
    private Integer versionInfoId;

    @Column(name = "android")
    private String androidVersion;

    @Column(name = "ios")
    private String iosVersion;

    @Column(name = "android_force_update")
    private Boolean androidForceUpgrade = false;

    @Column(name = "ios_force_update")
    private Boolean iosForceUpgrade = false;

    @Column(name = "app_id")
    private String appId;

    @Column(name = "app_name")
    private String appName;

    @Column(name = "org_id")
    private String orgId;

}
