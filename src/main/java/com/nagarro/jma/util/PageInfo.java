package com.nagarro.jma.util;

public  class PageInfo {
	private boolean hasNextPage;
    private boolean hasPreviousPage;
    private  long total;
	public PageInfo(boolean hasNextPage, boolean hasPreviousPage, long total) {
		super();
		this.hasNextPage = hasNextPage;
		this.hasPreviousPage = hasPreviousPage;
		this.total = total;
	}
	public boolean isHasNextPage() {
		return hasNextPage;
	}
	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}
	public boolean isHasPreviousPage() {
		return hasPreviousPage;
	}
	public void setHasPreviousPage(boolean hasPreviousPage) {
		this.hasPreviousPage = hasPreviousPage;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long l) {
		this.total = l;
	}
	public PageInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
}
