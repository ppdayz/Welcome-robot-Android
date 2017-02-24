package com.csjbot.welcomebot_zkhl.entity;

/**
 * Created by Administrator on 2016/11/03 0003.
 */

public class NaviGetPoseRspBean {

    /**
     * msg_id : NAVI_GET_POS_RSP
     * pos : {"x":10,"y":235,"z":25,"rotation":157}
     */

    private String msg_id = "NAVI_GET_POS_RSP";
    /**
     * x : 10
     * y : 235
     * z : 25
     * rotation : 157
     */

    private PosBean pos;

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public PosBean getPos() {
        return pos;
    }

    public void setPos(PosBean pos) {
        this.pos = pos;
    }

    public static class PosBean {
        private int x;
        private int y;
        private int z;
        private int rotation;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getZ() {
            return z;
        }

        public void setZ(int z) {
            this.z = z;
        }

        public int getRotation() {
            return rotation;
        }

        public void setRotation(int rotation) {
            this.rotation = rotation;
        }

        @Override
        public String toString() {
            return "x=" + x +
                    ", y=" + y +
                    ", z=" + z +
                    ", rotation=" + rotation;
        }
    }
}
