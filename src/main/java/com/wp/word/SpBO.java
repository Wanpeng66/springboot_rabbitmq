package com.wp.word;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * (SP)
 * 
 * @author bianj
 * @version 1.0.0 2019-03-26
 */
@Entity
@Table(name = "SP")
public class SpBO implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 8932406077501133423L;
    
    /**  */
    @Id
    @Column(name = "ID", unique = true, nullable = false, length = 10)
    private Integer ID;
    
    /**  */
    @Column(name = "SPID", nullable = false, length = 255)
    private String SPID;
    
    /**  */
    @Column(name = "SPMC", nullable = false, length = 65535)
    private String SPMC;
    
    /**  */
    @Column(name = "BZXT", nullable = true, length = 255)
    private String BZXT;
    
    /**  */
    @Column(name = "ZXGG", nullable = true, length = 255)
    private String ZXGG;
    
    /**  */
    @Column(name = "CPGG", nullable = true, length = 255)
    private String CPGG;
    
    /**  */
    @Column(name = "BZQ", nullable = true, length = 255)
    private String BZQ;
    
    /**  */
    @Column(name = "CD", nullable = true, length = 255)
    private String CD;
    
    /**  */
    @Column(name = "CCTJ", nullable = true, length = 255)
    private String CCTJ;
    
    /**  */
    @Column(name = "YSBZ", nullable = true, length = 255)
    private String YSBZ;
    
    /**  */
    @Column(name = "PZBZ", nullable = true, length = 255)
    private String PZBZ;
    
    /**
     * 获取
     * 
     * @return 
     */
    public Integer getID() {
        return this.ID;
    }
     
    /**
     * 设置
     * 
     * @param ID
     *          
     */
    public void setID(Integer ID) {
        this.ID = ID;
    }
    
    /**
     * 获取
     * 
     * @return 
     */
    public String getSPID() {
        return this.SPID;
    }
     
    /**
     * 设置
     * 
     * @param SPID
     *          
     */
    public void setSPID(String SPID) {
        this.SPID = SPID;
    }
    
    /**
     * 获取
     * 
     * @return 
     */
    public String getSPMC() {
        return this.SPMC;
    }
     
    /**
     * 设置
     * 
     * @param SPMC
     *          
     */
    public void setSPMC(String SPMC) {
        this.SPMC = SPMC;
    }
    
    /**
     * 获取
     * 
     * @return 
     */
    public String getBZXT() {
        return this.BZXT;
    }
     
    /**
     * 设置
     * 
     * @param BZXT
     *          
     */
    public void setBZXT(String BZXT) {
        this.BZXT = BZXT;
    }
    
    /**
     * 获取
     * 
     * @return 
     */
    public String getZXGG() {
        return this.ZXGG;
    }
     
    /**
     * 设置
     * 
     * @param ZXGG
     *          
     */
    public void setZXGG(String ZXGG) {
        this.ZXGG = ZXGG;
    }
    
    /**
     * 获取
     * 
     * @return 
     */
    public String getCPGG() {
        return this.CPGG;
    }
     
    /**
     * 设置
     * 
     * @param CPGG
     *          
     */
    public void setCPGG(String CPGG) {
        this.CPGG = CPGG;
    }
    
    /**
     * 获取
     * 
     * @return 
     */
    public String getBZQ() {
        return this.BZQ;
    }
     
    /**
     * 设置
     * 
     * @param BZQ
     *          
     */
    public void setBZQ(String BZQ) {
        this.BZQ = BZQ;
    }
    
    /**
     * 获取
     * 
     * @return 
     */
    public String getCD() {
        return this.CD;
    }
     
    /**
     * 设置
     * 
     * @param CD
     *          
     */
    public void setCD(String CD) {
        this.CD = CD;
    }
    
    /**
     * 获取
     * 
     * @return 
     */
    public String getCCTJ() {
        return this.CCTJ;
    }
     
    /**
     * 设置
     * 
     * @param CCTJ
     *          
     */
    public void setCCTJ(String CCTJ) {
        this.CCTJ = CCTJ;
    }
    
    /**
     * 获取
     * 
     * @return 
     */
    public String getYSBZ() {
        return this.YSBZ;
    }
     
    /**
     * 设置
     * 
     * @param YSBZ
     *          
     */
    public void setYSBZ(String YSBZ) {
        this.YSBZ = YSBZ;
    }
    
    /**
     * 获取
     * 
     * @return 
     */
    public String getPZBZ() {
        return this.PZBZ;
    }
     
    /**
     * 设置
     * 
     * @param PZBZ
     *          
     */
    public void setPZBZ(String PZBZ) {
        this.PZBZ = PZBZ;
    }
}