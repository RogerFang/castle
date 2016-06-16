package com.whenling.castle.repo.domain;

import java.util.List;

public class Node<T extends Hierarchical<T>> {

	private T data;
	private List<Node<T>> children;

	private Boolean checked;
	private Boolean expanded;
	private String iconCls;
	private String iconPath;

	public boolean getLeaf() {
		List<Node<T>> children = getChildren();
		return children == null || children.size() == 0;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public List<Node<T>> getChildren() {
		return children;
	}

	public void setChildren(List<Node<T>> children) {
		this.children = children;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public Boolean getExpanded() {
		return expanded;
	}

	public void setExpanded(Boolean expanded) {
		this.expanded = expanded;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

}
