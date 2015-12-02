package com.athaydes.web.dao;

public class CacheInfo {

	private String uuid;
	private String id;
	private String name;
	private String cluster;
	private long ts;
	private double throughput;
	private boolean isOutlier;
	
	public CacheInfo (String uuid, String id, String name, String cluster, long ts, double throughput) {
		this.uuid = uuid;
		this.id = id;
		this.name = name;
		this.cluster = cluster;
		this.ts = ts;
		this.throughput = throughput;
		this.setOutlier(false);
	}
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCluster() {
		return cluster;
	}
	public void setCluster(String cluster) {
		this.cluster = cluster;
	}
	public long getTs() {
		return ts;
	}
	public void setTs(long ts) {
		this.ts = ts;
	}
	public double getThroughput() {
		return throughput;
	}
	public void setThroughput(double throughput) {
		this.throughput = throughput;
	}
	
    @Override
    public String toString()
    {
        return "Cache Details [name = "+name+", throughput = "+throughput+", cluster = "+cluster+", uuid = "+uuid+"]";
    }

	public boolean isOutlier() {
		return isOutlier;
	}

	public void setOutlier(boolean isOutlier) {
		this.isOutlier = isOutlier;
	}
	
}
