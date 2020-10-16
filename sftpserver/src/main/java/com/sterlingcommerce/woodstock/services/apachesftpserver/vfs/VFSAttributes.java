package com.sterlingcommerce.woodstock.services.apachesftpserver.vfs;

import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.FileTime;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.sshd.common.subsystem.sftp.SftpConstants;
import org.apache.sshd.common.subsystem.sftp.SftpHelper;
import org.apache.sshd.common.util.GenericUtils;


public class VFSAttributes {
	
	//valid attribute flags
	private Set<Attribute> flags = EnumSet.noneOf(Attribute.class);
	private int type;
    private int permissions;
    private int uid;
    private int gid;
    private String owner;
    private String group;
    private long size;
    private FileTime accessTime;
    private FileTime createTime;
    private FileTime modifyTime;
    private List<AclEntry> acl;
    private Map<String, byte[]> extendedAttributes = Collections.emptyMap();
    
    enum Attribute {
        Size,
        UidGid,
        Perms,
        OwnerGroup,
        AccessTime,
        ModifyTime,
        CreateTime,
        Acl,
        Extensions
    }
    
    public VFSAttributes() {
        super();
    }

    public Set<Attribute> getFlags() {
        return flags;
    }

    public VFSAttributes addFlag(Attribute flag) {
        flags.add(flag);
        return this;
    }

    public VFSAttributes removeFlag(Attribute flag) {
        flags.remove(flag);
        return this;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getSize() {
        return size;
    }

    public VFSAttributes size(long size) {
        setSize(size);
        return this;
    }

    public void setSize(long size) {
        this.size = size;
        addFlag(Attribute.Size);
    }

    public String getOwner() {
        return owner;
    }

    public VFSAttributes owner(String owner) {
        setOwner(owner);
        return this;
    }

    public void setOwner(String owner) {
        this.owner = owner;
        /*
         * According to https://tools.ietf.org/wg/secsh/draft-ietf-secsh-filexfer/draft-ietf-secsh-filexfer-13.txt
         * section 7.5
         *
         * If either the owner or group field is zero length, the field should be considered absent, and no change
         * should be made to that specific field during a modification operation.
         */
        if (GenericUtils.isEmpty(owner)) {
            removeFlag(Attribute.OwnerGroup);
        } else {
            addFlag(Attribute.OwnerGroup);
        }
    }

    public String getGroup() {
        return group;
    }

    public VFSAttributes group(String group) {
        setGroup(group);
        return this;
    }

    public void setGroup(String group) {
        this.group = group;
        /*
         * According to https://tools.ietf.org/wg/secsh/draft-ietf-secsh-filexfer/draft-ietf-secsh-filexfer-13.txt
         * section 7.5
         *
         * If either the owner or group field is zero length, the field should be considered absent, and no change
         * should be made to that specific field during a modification operation.
         */
        if (GenericUtils.isEmpty(group)) {
            removeFlag(Attribute.OwnerGroup);
        } else {
            addFlag(Attribute.OwnerGroup);
        }
    }

    public int getUserId() {
        return uid;
    }

    public int getGroupId() {
        return gid;
    }

    public VFSAttributes owner(int uid, int gid) {
        this.uid = uid;
        this.gid = gid;
        addFlag(Attribute.UidGid);
        return this;
    }

    public int getPermissions() {
        return this.permissions;
    }

    public VFSAttributes perms(int perms) {
        setPermissions(perms);
        return this;
    }

    public void setPermissions(int perms) {
        this.permissions = perms;
        addFlag(Attribute.Perms);
    }

    public FileTime getAccessTime() {
        return accessTime;
    }

    public VFSAttributes accessTime(long atime) {
        return accessTime(atime, TimeUnit.SECONDS);
    }

    public VFSAttributes accessTime(long atime, TimeUnit unit) {
        return accessTime(FileTime.from(atime, unit));
    }

    public VFSAttributes accessTime(FileTime atime) {
        setAccessTime(atime);
        return this;
    }

    public void setAccessTime(FileTime atime) {
        accessTime = Objects.requireNonNull(atime, "No access time");
        addFlag(Attribute.AccessTime);
    }

    public FileTime getCreateTime() {
        return createTime;
    }

    public VFSAttributes createTime(long ctime) {
        return createTime(ctime, TimeUnit.SECONDS);
    }

    public VFSAttributes createTime(long ctime, TimeUnit unit) {
        return createTime(FileTime.from(ctime, unit));
    }

    public VFSAttributes createTime(FileTime ctime) {
        setCreateTime(ctime);
        return this;
    }

    public void setCreateTime(FileTime ctime) {
        createTime = Objects.requireNonNull(ctime, "No create time");
        addFlag(Attribute.CreateTime);
    }

    public FileTime getModifyTime() {
        return modifyTime;
    }

    public VFSAttributes modifyTime(long mtime) {
        return modifyTime(mtime, TimeUnit.SECONDS);
    }

    public VFSAttributes modifyTime(long mtime, TimeUnit unit) {
        return modifyTime(FileTime.from(mtime, unit));
    }

    public VFSAttributes modifyTime(FileTime mtime) {
        setModifyTime(mtime);
        return this;
    }

    public void setModifyTime(FileTime mtime) {
        modifyTime = Objects.requireNonNull(mtime, "No modify time");
        addFlag(Attribute.ModifyTime);
    }

    public List<AclEntry> getAcl() {
        return acl;
    }

    public VFSAttributes acl(List<AclEntry> acl) {
        setAcl(acl);
        return this;
    }

    public void setAcl(List<AclEntry> acl) {
        this.acl = Objects.requireNonNull(acl, "No ACLs");
        addFlag(Attribute.Acl);
    }

    public Map<String, byte[]> getExtensions() {
        return this.extendedAttributes;
    }

    public VFSAttributes extensions(Map<String, byte[]> extensions) {
        setExtensions(extensions);
        return this;
    }

    public void setStringExtensions(Map<String, String> extensions) {
        setExtensions(SftpHelper.toBinaryExtensions(extensions));
    }

    public void setExtensions(Map<String, byte[]> extensions) {
        this.extendedAttributes = Objects.requireNonNull(extensions, "No extensions");
        addFlag(Attribute.Extensions);
    }

    public boolean isRegularFile() {
        return (getPermissions() & SftpConstants.S_IFMT) == SftpConstants.S_IFREG;
    }

    public boolean isDirectory() {
        return (getPermissions() & SftpConstants.S_IFMT) == SftpConstants.S_IFDIR;
    }

    public boolean isSymbolicLink() {
        return (getPermissions() & SftpConstants.S_IFMT) == SftpConstants.S_IFLNK;
    }

    public boolean isOther() {
        return !isRegularFile() && !isDirectory() && !isSymbolicLink();
    }

    @Override
    public String toString() {
        return "type=" + getType() + ";size=" + getSize() + ";uid=" + getUserId() + ";gid=" + getGroupId() + ";perms=0x"
               + Integer.toHexString(getPermissions()) + ";flags=" + getFlags() + ";owner=" + getOwner() + ";group="
               + getGroup() + ";aTime=" + getAccessTime() + ";cTime=" + getCreateTime() + ";mTime=" + getModifyTime()
               + ";extensions=" + getExtensions().keySet();
    }

}
