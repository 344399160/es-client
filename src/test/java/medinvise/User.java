package medinvise;

import cn.com.deepdata.elasticsearch.annotations.Document;
import cn.com.deepdata.elasticsearch.annotations.Field;
import cn.com.deepdata.elasticsearch.annotations.ID;
import lombok.Data;

@Data
@Document(indexName = "user", type = "detail")
public class User {
	@ID
	private String uid;

	private String nickname;//昵称

	@Field(fieldData = true)
	private String name;//名字

	private String idcard;//身份证

	@Field(fieldData = true)
	private String phone;//电话

	private String head_image;//base64存储的头像
	
	private String head_image_url;//存放地址

	private long registerTime = System.currentTimeMillis(); //注册时间

	private String email;  //邮箱

}
