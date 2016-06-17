/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package whitepages.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author GLB-029
 */
@Entity
@Table(name = "TertiaryUrl")
@NamedQueries({
    @NamedQuery(name = "TertiaryUrl.findAll", query = "SELECT p FROM TertiaryUrl p")})
public class TertiaryUrl implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "url", unique = true)
    private String url;
    @Column(name = "scraped")
    private boolean scraped;
    
    @Column(name = "name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isScraped() {
        return scraped;
    }

    public void setScraped(boolean scraped) {
        this.scraped = scraped;
    }

    public TertiaryUrl() {
    }

    public TertiaryUrl(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String profileurl) {
        this.url = profileurl;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TertiaryUrl)) {
            return false;
        }
        TertiaryUrl other = (TertiaryUrl) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "whitepages.entity.Profileurl[ id=" + id + " ]";
    }

}
