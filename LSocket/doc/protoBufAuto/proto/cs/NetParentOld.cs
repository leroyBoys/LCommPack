//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated by a tool.
//
//     Changes to this file may cause incorrect behavior and will be lost if
//     the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

// Generated from: proto/NetParentOld.proto
namespace proto.NetParentOld
{
  [global::System.Serializable, global::ProtoBuf.ProtoContract(Name=@"NetCommond")]
  public partial class NetCommond : global::ProtoBuf.IExtensible
  {
    public NetCommond() {}
    
    private string _sn = "";
    [global::ProtoBuf.ProtoMember(1, IsRequired = false, Name=@"sn", DataFormat = global::ProtoBuf.DataFormat.Default)]
    [global::System.ComponentModel.DefaultValue("")]
    public string sn
    {
      get { return _sn; }
      set { _sn = value; }
    }
    private int _cmd = default(int);
    [global::ProtoBuf.ProtoMember(2, IsRequired = false, Name=@"cmd", DataFormat = global::ProtoBuf.DataFormat.TwosComplement)]
    [global::System.ComponentModel.DefaultValue(default(int))]
    public int cmd
    {
      get { return _cmd; }
      set { _cmd = value; }
    }
    private int _status = default(int);
    [global::ProtoBuf.ProtoMember(3, IsRequired = false, Name=@"status", DataFormat = global::ProtoBuf.DataFormat.TwosComplement)]
    [global::System.ComponentModel.DefaultValue(default(int))]
    public int status
    {
      get { return _status; }
      set { _status = value; }
    }
    private int _time = default(int);
    [global::ProtoBuf.ProtoMember(4, IsRequired = false, Name=@"time", DataFormat = global::ProtoBuf.DataFormat.TwosComplement)]
    [global::System.ComponentModel.DefaultValue(default(int))]
    public int time
    {
      get { return _time; }
      set { _time = value; }
    }
    private byte[] _obj = null;
    [global::ProtoBuf.ProtoMember(5, IsRequired = false, Name=@"obj", DataFormat = global::ProtoBuf.DataFormat.Default)]
    [global::System.ComponentModel.DefaultValue(null)]
    public byte[] obj
    {
      get { return _obj; }
      set { _obj = value; }
    }
    private int _uid = default(int);
    [global::ProtoBuf.ProtoMember(6, IsRequired = false, Name=@"uid", DataFormat = global::ProtoBuf.DataFormat.TwosComplement)]
    [global::System.ComponentModel.DefaultValue(default(int))]
    public int uid
    {
      get { return _uid; }
      set { _uid = value; }
    }
    private int _seq = default(int);
    [global::ProtoBuf.ProtoMember(7, IsRequired = false, Name=@"seq", DataFormat = global::ProtoBuf.DataFormat.TwosComplement)]
    [global::System.ComponentModel.DefaultValue(default(int))]
    public int seq
    {
      get { return _seq; }
      set { _seq = value; }
    }
    private global::ProtoBuf.IExtension extensionObject;
    global::ProtoBuf.IExtension global::ProtoBuf.IExtensible.GetExtensionObject(bool createIfMissing)
      { return global::ProtoBuf.Extensible.GetExtensionObject(ref extensionObject, createIfMissing); }
  }
  
}