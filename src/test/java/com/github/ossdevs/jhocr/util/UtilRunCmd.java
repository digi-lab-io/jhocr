package com.github.ossdevs.jhocr.util;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * TODO add documentation
 */
public class UtilRunCmd {

    private String out = null;
    private int exitStatus = -1;

    public UtilRunCmd() {
    }

    /**
     * TODO add documentation
     *
     * @param cmd
     * @return
     */
    public int run(String cmd) {
        try {

            CommandLine commandline = CommandLine.parse(cmd);
            DefaultExecutor exec = new DefaultExecutor();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);

            exec.setStreamHandler(streamHandler);

            exitStatus = exec.execute(commandline);
            out = outputStream.toString();

            return exitStatus;

        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }

    }

    public String getResult() {
        return out.toString();
    }

    public int getExitStatus() {
        return exitStatus;
    }
}
