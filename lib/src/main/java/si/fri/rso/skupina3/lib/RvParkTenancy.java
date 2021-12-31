package si.fri.rso.skupina3.lib;

import java.util.Date;

public class RvParkTenancy {

    private Integer park_tenancy_id;
    private Integer user_id;
    private Integer park_id;
    private Date start_date;
    private Date end_date;
    private Integer rv_park_bill_id;

    public Integer getPark_tenancy_id() {
        return park_tenancy_id;
    }

    public void setPark_tenancy_id(Integer park_tenancy_id) {
        this.park_tenancy_id = park_tenancy_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getPark_id() {
        return park_id;
    }

    public void setPark_id(Integer park_id) {
        this.park_id = park_id;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public Integer getRv_park_bill_id() {
        return rv_park_bill_id;
    }

    public void setRv_park_bill_id(Integer rv_park_bill_id) {
        this.rv_park_bill_id = rv_park_bill_id;
    }
}
