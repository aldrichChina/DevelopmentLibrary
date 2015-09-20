package com.fairlink.passenger.bean;

/**
 * @ClassName ： RecVideo
 * @Description: 首页视频推荐数据类
 */

public class RecVideo {

	private String name; // 视频名称
	private String id; // 视频id
	private int type; // 视频类型
	private String url; // 视频地址
	private String img; // 视频图片
	private int position; // 视频播放位置

	public RecVideo(String name, String id, int type, String url, String img, int position) {
		super();
		this.name = name;
		this.id = id;
		this.type = type;
		this.url = url;
		this.img = img;
		this.position = position;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

}
