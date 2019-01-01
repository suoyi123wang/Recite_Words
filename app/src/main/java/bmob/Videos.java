package bmob;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Videos extends BmobObject {
    private String name;
    private String videotype;
    private BmobFile videocover;
    private BmobFile video;
    private String info;


    public String getName() {
        return name;
    }

    public String getinfo(){
        return info;
    }

    public String getVideo(){
        return video.getFileUrl();
    }

    public String getType() {
        return videotype;
    }


    public String getImage(){
        return videocover.getFileUrl();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.videotype = type;
    }


}
