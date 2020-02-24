package com.myproject.csvwriter.scheduler;

import com.jcraft.jsch.*;
import com.jcraft.jsch.ChannelSftp.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

@Component
public class CsvReaderScheduler {

    @Scheduled(fixedRate = 60000)
    public void cronCsvReader() {
        List<String> listFolder = new ArrayList<>();

        String user = "admin";
        String host = "172.25.230.12";
        String password = "asyst";
        String path = "/home/admin/files";

        JSch jsch = new JSch();
        Session session = null;

        try {
            session = jsch.getSession(user, host, 22);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(password);
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel;

            listDirectory(sftpChannel, path, listFolder);

            for(String pathToRead : listFolder) {
                InputStream stream = sftpChannel.get(pathToRead);
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(stream));
                    String line;
                    while ((line = br.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException io) {
                    System.out.println("Exception occurred during reading file from SFTP server due to " + io.getMessage());
                    io.getMessage();
                } catch (Exception e) {
                    System.out.println("Exception occurred during reading file from SFTP server due to " + e.getMessage());
                    e.getMessage();
                }
            }

            sftpChannel.exit();
            session.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        }
    }

    public static void listDirectory(ChannelSftp channelSftp, String path, List<String> list) throws SftpException {
        Vector<LsEntry> files = channelSftp.ls(path);
        for (LsEntry entry : files) {
            if (!entry.getAttrs().isDir()) {
                list.add(path + "/" + entry.getFilename());
            } else {
                if (!entry.getFilename().equals(".") && !entry.getFilename().equals("..")) {
                    listDirectory(channelSftp, path + "/" + entry.getFilename(), list);
                }
            }
        }
    }
}
