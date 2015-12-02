package com.athaydes.web.dao;

public class CacheThroughput {
	private String ts;

	private String value;

	private String uuid;

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public String toString() {
		return "Cache Throughput [ts = " + ts + ", value = " + value + ", uuid = " + uuid + "]";
	}
}
