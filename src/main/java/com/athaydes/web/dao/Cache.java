package com.athaydes.web.dao;

public class Cache {
    private String id;

    private String ts;

    private String cache;

    private String opStatus;

    private String cluster;

    private String uuid;

    private String hwRevision;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getTs ()
    {
        return ts;
    }

    public void setTs (String ts)
    {
        this.ts = ts;
    }


    public String getOpStatus ()
    {
        return opStatus;
    }

    public void setOpStatus (String opStatus)
    {
        this.opStatus = opStatus;
    }

	public String getCache() {
		return cache;
	}

	public void setCache(String cache) {
		this.cache = cache;
	}

    public String getCluster ()
    {
        return cluster;
    }

    public void setCluster (String cluster)
    {
        this.cluster = cluster;
    }

    public String getUuid ()
    {
        return uuid;
    }

    public void setUuid (String uuid)
    {
        this.uuid = uuid;
    }

    public String getHwRevision ()
    {
        return hwRevision;
    }

    public void setHwRevision (String hwRevision)
    {
        this.hwRevision = hwRevision;
    }

    @Override
    public String toString()
    {
        return "Cache [id = "+id+", ts = "+ts+", name = "+cache+", opStatus = "+opStatus+", cluster = "+cluster+", uuid = "+uuid+", hwRevision = "+hwRevision+"]";
    }
}
