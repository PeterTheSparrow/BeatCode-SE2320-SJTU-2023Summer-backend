package team.beatcode.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.beatcode.consumer.socket.PushHandler;

@RestController
@CrossOrigin("*")
@RequestMapping("/socket")
public class SocketController {
    @Autowired
    PushHandler pushHandler;

    @RequestMapping("inform")
    public void judgeFinished(@RequestParam Integer uid, @RequestParam String pid) {
        pushHandler.sendTextToUser(uid,
                String.format(
                        "{\"user\": %d, \"problem\": %s}",
                        uid,
                        pid
                        ));
    }
}
