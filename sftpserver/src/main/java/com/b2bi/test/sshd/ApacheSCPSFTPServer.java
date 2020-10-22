package com.b2bi.test.sshd;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.scp.ScpCommandFactory;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory;

import com.sterlingcommerce.woodstock.services.apachesftpserver.vfs.ExchangeFileSystemFactory;
import com.sterlingcommerce.woodstock.services.apachesftpserver.vfs.VFSEventListener;
import com.sterlingcommerce.woodstock.services.apachesftpserver.vfs.filessytem.CustomSFTPFileSystemAccessor;
import com.sterlingcommerce.woodstock.services.apachesftpserver.vfs.filessytem.CustomSCPFileOpener;

public class ApacheSCPSFTPServer {

	public static void start() {
		try {
			SshServer sshd = SshServer.setUpDefaultServer();
			sshd.setPort(1111);
			sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(new File("host.ser").toPath()));

			VFSEventListener eventListner = new VFSEventListener();
			ScpCommandFactory factory = new ScpCommandFactory.Builder().withFileOpener(new CustomSCPFileOpener())
					.build();
			factory.addEventListener(eventListner);
			sshd.setCommandFactory(factory);
			sshd.setShellFactory(factory);

			SftpSubsystemFactory sftpFactory = new SftpSubsystemFactory.Builder()
					.withFileSystemAccessor(new CustomSFTPFileSystemAccessor()).build();
			sftpFactory.addSftpEventListener(eventListner);
			sshd.setSubsystemFactories(Arrays.asList(sftpFactory));

			Path homeDir = Paths.get("C:\\\\ApacheSCPTest");
			if (!Files.exists(homeDir)) {
				Files.createDirectories(homeDir);
			}

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
