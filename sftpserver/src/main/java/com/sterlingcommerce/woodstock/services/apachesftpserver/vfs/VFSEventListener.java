package com.sterlingcommerce.woodstock.services.apachesftpserver.vfs;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Collection;
import java.util.Set;

import org.apache.sshd.common.scp.ScpTransferEventListener;
import org.apache.sshd.common.session.Session;
import org.apache.sshd.server.subsystem.sftp.SftpEventListener;
import org.apache.sshd.server.session.ServerSession;

public class VFSEventListener implements ScpTransferEventListener, SftpEventListener {

	//start of SCP events
    @Override
    public void startFileEvent(Session session, FileOperation fileOperation, Path path, long l, Set<PosixFilePermission> set) {
        System.out.println("startFileEvent (" + (fileOperation == FileOperation.SEND ? "SEND" : "RECEIVE") + ") " + path);
    }

    @Override
    public void endFileEvent(Session session, FileOperation fileOperation, Path path, long l, Set<PosixFilePermission> set, Throwable throwable) {
        System.out.println("endFileEvent (" + (fileOperation == FileOperation.SEND ? "SEND" : "RECEIVE") + ") " + path);
        System.out.println("userName:"+session.getUsername());
       
        if(throwable != null) {
            throwable.printStackTrace();
            return;
        }

    }

    @Override
    public void startFolderEvent(Session session, FileOperation fileOperation, Path path, Set<PosixFilePermission> set) {
        System.out.println("startFolderEvent (" + (fileOperation == FileOperation.SEND ? "SEND" : "RECEIVE") + ") " + path);
    }

    @Override
    public void endFolderEvent(Session session, FileOperation fileOperation, Path path, Set<PosixFilePermission> set, Throwable throwable) {
        System.out.println("endFolderEvent (" + (fileOperation == FileOperation.SEND ? "SEND" : "RECEIVE") + ") " + path);
        if(throwable != null) {
            throwable.printStackTrace();
        }
    }
    
    //end of SCP events
    
    //start of SFTP events
    @Override
    public void moving(
            ServerSession session, Path srcPath, Path dstPath, Collection<CopyOption> opts)
            throws IOException {
        System.out.println(" start moving file: "+srcPath.toString() + " "+dstPath.toString());
    }
    
    @Override
    public void moved(
            ServerSession session, Path srcPath, Path dstPath, Collection<CopyOption> opts, Throwable thrown)
            throws IOException {
        System.out.println(" moved : "+srcPath.toString() + " "+dstPath.toString());
    }
    //end of SFTP evnets
}
