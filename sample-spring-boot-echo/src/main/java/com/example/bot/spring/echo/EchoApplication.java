/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.example.bot.spring.echo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.awt.Rectangle;
import java.awt.Toolkit;

import java.net.*;



@SpringBootApplication
@LineMessageHandler
public class EchoApplication {
    private static final String INVITE_MAIN = "i";
    private static final String SCREEN_MAIN = "sc";
    private static final String RESTART_MAIN = "re";
    private static final String RIFT_MAIN = "rift";
    static {

            System.setProperty("java.awt.headless", "false");
    }
    public static void main(String[] args) {
        SpringApplication.run(EchoApplication.class, args);
    }

	public byte[] toByteArray(InputStream in) throws IOException {

		ByteArrayOutputStream os = new ByteArrayOutputStream();

		byte[] buffer = new byte[1024];
		int len;

		// read bytes from the input stream and store them in buffer
		while ((len = in.read(buffer)) != -1) {
			// write bytes from the buffer into output stream
			os.write(buffer, 0, len);
		}

		return os.toByteArray();
	}

    @GetMapping(value = "/image")
    public @ResponseBody byte[] getImage() throws IOException {
        InputStream in = getClass()
          .getResourceAsStream("c:\\image.jpg");
        return toByteArray(in);
    }

   @RequestMapping(value = "/", method = RequestMethod.POST)
   public Message screenshot() throws IOException {

//Create image file here
        try{
            Robot r = new Robot();
            String path = "c:/TMP/temp.jpg"; 
            
            Rectangle capture =  new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()); 
            BufferedImage Image = r.createScreenCapture(capture); 
            File f = new File(path);
            ImageIO.write(Image, "jpg", f); 
            return new ImageMessage(new URI("file:///C:/TMP/temp.jpg"),new URI("file:///C:/TMP/temp.jpg"));
        }catch(Exception ex) {
            ex.printStackTrace();
        }
//Other part, test for image replyMessage
		// 返信用画像(image)のURL
//		final String originalContentUrl = "https://c477d152.ngrok.io/original.jpg";
//		final String previewImageUrl = "https://c477d152.ngrok.io/small.jpg";

		// 返信用画像(image)メッセージ
//		ImageMessage imageMessage = new ImageMessage(originalContentUrl, previewImageUrl);

		// テキストメッセージと画像を同時に返す
//		return new ReplyMessage(event.getReplyToken(), Arrays.asList(imageMessage, textMessage));
        
        return null;
   }

    @EventMapping
    public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        String message = event.getMessage().getText();
        try{
            
//Do image return test
        if (message != null && message.trim().length() > 0 && message.toLowerCase().startsWith(SCREEN_MAIN)){
            return screenshot();
        }
        //invite
        if (message != null && message.trim().length() > 0 && message.toLowerCase().startsWith(INVITE_MAIN)){
            String replyMessage = "";
            String command = message.toLowerCase().replace(INVITE_MAIN, "").trim();
            Robot r = new Robot();
            replyMessage = String.format("complete send invite command!", event.getSource().getSenderId());
            r.delay(100);
            r.keyPress(KeyEvent.VK_EQUALS);
            r.keyRelease(KeyEvent.VK_EQUALS);
            return new TextMessage(replyMessage);
        }
        //restart
        if (message != null && message.trim().length() > 0 && message.toLowerCase().startsWith(RESTART_MAIN)){
            String replyMessage = "";
            String command = message.toLowerCase().replace(RESTART_MAIN, "").trim();
            Robot r = new Robot();
            replyMessage = String.format("complete send restart command!", event.getSource().getSenderId());
            r.delay(100);
            r.keyPress(KeyEvent.VK_BACK_SLASH);
            r.keyRelease(KeyEvent.VK_BACK_SLASH);
            return new TextMessage(replyMessage);
        }
        //restart
        if (message != null && message.trim().length() > 0 && message.toLowerCase().startsWith(RIFT_MAIN)){
            String replyMessage = "";
            String command = message.toLowerCase().replace(RIFT_MAIN, "").trim();
            Robot r = new Robot();
            replyMessage = String.format("complete send restart->rift command!", event.getSource().getSenderId());
            r.delay(100);
            r.keyPress(KeyEvent.VK_MINUS);
            r.keyRelease(KeyEvent.VK_MINUS);
            return new TextMessage(replyMessage);
        }
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }
}
/*

message = ImageSendMessage(
    original_content_url='https://example.com/original.jpg',
    preview_image_url='https://example.com/preview.jpg'
)
line_bot_api.reply_message(event.reply_token, message)


import requests
import urllib.parse
import sys

LINE_ACCESS_TOKEN="LINE_ACCESS_TOKEN"
url = "https://notify-api.line.me/api/notify"
file = {'imageFile':open('Parth','rb')}
data = ({
        'message':'Test Image'
    })
LINE_HEADERS = {"Authorization":"Bearer "+LINE_ACCESS_TOKEN}
session = requests.Session()
r=session.post(url, headers=LINE_HEADERS, files=file, data=data)
print(r.text)
*/