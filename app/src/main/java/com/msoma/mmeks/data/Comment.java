package com.msoma.mmeks.data;


public class Comment {
    private int organisation_id,portfolio_id;
    private String name,comment;

    public Comment() {
    }

    public Comment(int organisation_id, int portfolio_id,String name,String comment) {
        super();
        this.organisation_id = organisation_id;
        this.portfolio_id = portfolio_id;
        this.name = name;
        this.comment = comment;

    }
   public int getId() {
        return organisation_id;
    }

    public void setId(int organisation_id) {
        this.organisation_id = organisation_id;
    }

    public int getPortId () {

        return  portfolio_id;
    }


    public void  setPortId(int portfolio_id) {
        this.portfolio_id = portfolio_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment () {
        return comment;
    }


    public void  setComment (String comment) {

        this.comment = comment;

    }
}
