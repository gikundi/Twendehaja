package com.msoma.mmeks.data;

public class FeedItem {
	private int organisation_id,portfolio_id;
	private String name, status, image, profilePic, timeStamp, url,OrganisationName,location, commentCount,comment;

	public FeedItem() {
	}

	public FeedItem(int organisation_id, int portfolio_id,String name, String image, String status,String location,String OrganisationName,
			String profilePic, String timeStamp, String url, String commentCount,String comment) {
		super();
		this.organisation_id = organisation_id;
        this.portfolio_id = portfolio_id;
		this.name = name;
        this.comment = comment;
        this.OrganisationName = OrganisationName;
        this.location = location;
		this.image = image;
		this.status = status;
		this.profilePic = profilePic;
		this.timeStamp = timeStamp;
		this.url = url;
        this.commentCount = commentCount;
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

    public String getOrganisationName() {
        return OrganisationName;
    }


    public void setOrganisationName(String organisationName) {
       this.OrganisationName = organisationName;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImge() {
		return image;
	}

	public void setImge(String image) {
		this.image = image;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

    public String getComment () {
        return comment;
    }


    public void  setComment (String comment) {

        this.comment = comment;

    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }
}
