package org.springbus.test.netty.cmd;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CmdHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String ret = exeCmd(msg.toString());
        ctx.writeAndFlush(ret);
        super.channelRead(ctx, msg);
    }


    private String exeCmd(String commandStr) {

        if( commandStr.trim().equals("")) {
            return "";
        }
        BufferedReader br = null;
        Process p =null;
        try {
              p = Runtime.getRuntime().exec(commandStr);
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if(p!=null){
                p.destroy();
            }
        }
        return "error";
    }


}
