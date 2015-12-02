package com.athaydes.web.dao;

import java.util.List;

public class Cluster {

	private String name;
	private List<CacheInfo> cacheInfoList;
	

	public Cluster(String name, List<CacheInfo> list) {
		this.name = name;
		this.cacheInfoList = list;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<CacheInfo> getCacheInfoList() {
		return cacheInfoList;
	}
	
	public void setCacheInfoList(List<CacheInfo> cacheInfoList) {
		this.cacheInfoList = cacheInfoList;
	}
	
	public void calcOutlier(float threshold) {
		double sum = 0;
		for (CacheInfo info : cacheInfoList) {
			sum += info.getThroughput();
		}
		double avg = sum / cacheInfoList.size();
		for (CacheInfo info : cacheInfoList) {
			if (isCacheOutlier(info, threshold, avg)) {
				info.setOutlier(true);
			}
		}
	}
	
	public void printOutlier()
	{
		System.out.println("Cluster [" + name + "] outlier:");
		for (CacheInfo info : cacheInfoList) {
			if (info.isOutlier()) {
				System.out.println("\t " + info.toString());
			}
		}
	}
	
	private boolean isCacheOutlier(CacheInfo info, float threshold, double avg)
	{
		return (info.getThroughput() > (avg * (1 + threshold / 100)) || info.getThroughput() < (avg * (1 - threshold / 100)));
	}
}

