package team.beatcode.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.beatcode.consumer.socket.PushHandler;

@RestController
@RequestMapping("/socket")
public class SocketController {
    @Autowired
    PushHandler pushHandler;

    @RequestMapping("inform")
    public void judgeFinished(@RequestParam Integer uid, @RequestParam String msg) {
        pushHandler.sendTextToUser(uid, msg);
    }
}
