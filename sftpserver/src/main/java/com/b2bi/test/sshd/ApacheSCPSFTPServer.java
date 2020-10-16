package com.b2bi.test.sshd;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import org.apache.sshd.common.file.FileSystemFactory;
import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.scp.ScpCommandFactory;
import org.apache.sshd.server.shell.ProcessShellCommandFactory;
import org.apache.sshd.server.shell.ProcessShellFactory;
import org.apache.sshd.server.shell.ShellFactory;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory;

import com.sterlingcommerce.woodstock.services.apachesftpserver.vfs.ExchangeFileSystemFactory;
import com.sterlingcommerce.woodstock.services.apachesftpserver.vfs.VFSEventListener;

public class ApacheSCPSFTPServer {

	public static void start() {
		try {
			SshServer sshd = SshServer.setUpDefaultServer();
			sshd.setPort(1111);
			sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(new File("host.ser").toPath()));

			VFSEventListener eventListner = new VFSEventListener();
			ScpCommandFactory factory = new ScpCommandFactory();
			factory.addEventListener(eventListner);

			sshd.setCommandFactory(factory);
			sshd.setShellFactory(factory);

			SftpSubsystemFactory sftpFactory = new SftpSubsystemFactory();
			sftpFactory.addSftpEventListener(eventListner);
			sshd.setSubsystemFactories(Collections.singletonList(sftpFactory));

			Path homeDir = Paths.get("C:\\\\ApacheSCPTest");
			if (!Files.exists(homeDir)) {
				Files.createDirectories(homeDir);
			}

			/*
			 * VirtualFileSystemFactory vfSysFactory = new VirtualFileSystemFactory();
			 * vfSysFactory.setDefaultHomeDir(homeDir); FileSystemFactory fileSystemFactory
			 * = vfSysFactory; sshd.setFileSystemFactory(fileSystemFactory);
			 */

			ExchangeFileSystemFactory vfsFactory = new ExchangeFileSystemFactory();
			vfsFactory.setDefaultHomeDir(homeDir);
			sshd.setFileSystemFactory(vfsFactory);

			sshd.setPasswordAuthenticator(
					(username, password, session) -> username.equals("admin") && password.equals("password"));

			sshd.start();

			System.out.println("sshd.getCommandFactory():" + sshd.getCommandFactory());
			System.out.println("sshd.getSubsystemFactories()" + sshd.getSubsystemFactories());
			System.out.println("channels : " + sshd.getChannelFactories());
			System.out.println("server started");

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static void main(String[] args) {
		start();
	}
}
