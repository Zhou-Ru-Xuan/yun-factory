package org.zhouruxuan.entity.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserQuery implements Serializable {

	private static final long serialVersionUID = 1L;

	private String username;

	private String password;

	private String realname;

	private String phone;

	private String begin;//注意，这里使用的是String类型，前端传过来的数据无需进行类型转换

	private String end;
}