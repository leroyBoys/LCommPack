//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated by a tool.
//
//     Changes to this file may cause incorrect behavior and will be lost if
//     the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

// Generated from: proto/Com.proto
namespace proto.Com
{
  [global::System.Serializable, global::ProtoBuf.ProtoContract(Name=@"NetOprateData")]
  public partial class NetOprateData : global::ProtoBuf.IExtensible
  {
    public NetOprateData() {}
    
    private int _otype;
    [global::ProtoBuf.ProtoMember(1, IsRequired = true, Name=@"otype", DataFormat = global::ProtoBuf.DataFormat.TwosComplement)]
    public int otype
    {
      get { return _otype; }
      set { _otype = value; }
    }
    private int _uid = default(int);
    [global::ProtoBuf.ProtoMember(2, IsRequired = false, Name=@"uid", DataFormat = global::ProtoBuf.DataFormat.TwosComplement)]
    [global::System.ComponentModel.DefaultValue(default(int))]
    public int uid
    {
      get { return _uid; }
      set { _uid = value; }
    }
    private int _dval = default(int);
    [global::ProtoBuf.ProtoMember(3, IsRequired = false, Name=@"dval", DataFormat = global::ProtoBuf.DataFormat.TwosComplement)]
    [global::System.ComponentModel.DefaultValue(default(int))]
    public int dval
    {
      get { return _dval; }
      set { _dval = value; }
    }
    private int _flag = default(int);
    [global::ProtoBuf.ProtoMember(4, IsRequired = false, Name=@"flag", DataFormat = global::ProtoBuf.DataFormat.TwosComplement)]
    [global::System.ComponentModel.DefaultValue(default(int))]
    public int flag
    {
      get { return _flag; }
      set { _flag = value; }
    }
    private readonly global::System.Collections.Generic.List<int> _dlist = new global::System.Collections.Generic.List<int>();
    [global::ProtoBuf.ProtoMember(5, Name=@"dlist", DataFormat = global::ProtoBuf.DataFormat.TwosComplement)]
    public global::System.Collections.Generic.List<int> dlist
    {
      get { return _dlist; }
    }
  
    private readonly global::System.Collections.Generic.List<NetKVData> _kvData = new global::System.Collections.Generic.List<NetKVData>();
    [global::ProtoBuf.ProtoMember(6, Name=@"kvData", DataFormat = global::ProtoBuf.DataFormat.Default)]
    public global::System.Collections.Generic.List<NetKVData> kvData
    {
      get { return _kvData; }
    }
  
    private global::ProtoBuf.IExtension extensionObject;
    global::ProtoBuf.IExtension global::ProtoBuf.IExtensible.GetExtensionObject(bool createIfMissing)
      { return global::ProtoBuf.Extensible.GetExtensionObject(ref extensionObject, createIfMissing); }
  }
  
  [global::System.Serializable, global::ProtoBuf.ProtoContract(Name=@"NetKVData")]
  public partial class NetKVData : global::ProtoBuf.IExtensible
  {
    public NetKVData() {}
    
    private int _k = default(int);
    [global::ProtoBuf.ProtoMember(1, IsRequired = false, Name=@"k", DataFormat = global::ProtoBuf.DataFormat.TwosComplement)]
    [global::System.ComponentModel.DefaultValue(default(int))]
    public int k
    {
      get { return _k; }
      set { _k = value; }
    }
    private int _v = default(int);
    [global::ProtoBuf.ProtoMember(2, IsRequired = false, Name=@"v", DataFormat = global::ProtoBuf.DataFormat.TwosComplement)]
    [global::System.ComponentModel.DefaultValue(default(int))]
    public int v
    {
      get { return _v; }
      set { _v = value; }
    }
    private readonly global::System.Collections.Generic.List<int> _dlist = new global::System.Collections.Generic.List<int>();
    [global::ProtoBuf.ProtoMember(3, Name=@"dlist", DataFormat = global::ProtoBuf.DataFormat.TwosComplement)]
    public global::System.Collections.Generic.List<int> dlist
    {
      get { return _dlist; }
    }
  
    private global::ProtoBuf.IExtension extensionObject;
    global::ProtoBuf.IExtension global::ProtoBuf.IExtensible.GetExtensionObject(bool createIfMissing)
      { return global::ProtoBuf.Extensible.GetExtensionObject(ref extensionObject, createIfMissing); }
  }
  
  [global::System.Serializable, global::ProtoBuf.ProtoContract(Name=@"NetResponse")]
  public partial class NetResponse : global::ProtoBuf.IExtensible
  {
    public NetResponse() {}
    
    private int _status = default(int);
    [global::ProtoBuf.ProtoMember(1, IsRequired = false, Name=@"status", DataFormat = global::ProtoBuf.DataFormat.TwosComplement)]
    [global::System.ComponentModel.DefaultValue(default(int))]
    public int status
    {
      get { return _status; }
      set { _status = value; }
    }
    private readonly global::System.Collections.Generic.List<NetOprateData> _operateDatas = new global::System.Collections.Generic.List<NetOprateData>();
    [global::ProtoBuf.ProtoMember(2, Name=@"operateDatas", DataFormat = global::ProtoBuf.DataFormat.Default)]
    public global::System.Collections.Generic.List<NetOprateData> operateDatas
    {
      get { return _operateDatas; }
    }
  
    private int _retStatus;
    [global::ProtoBuf.ProtoMember(3, IsRequired = true, Name=@"retStatus", DataFormat = global::ProtoBuf.DataFormat.TwosComplement)]
    public int retStatus
    {
      get { return _retStatus; }
      set { _retStatus = value; }
    }
    private int _step = default(int);
    [global::ProtoBuf.ProtoMember(4, IsRequired = false, Name=@"step", DataFormat = global::ProtoBuf.DataFormat.TwosComplement)]
    [global::System.ComponentModel.DefaultValue(default(int))]
    public int step
    {
      get { return _step; }
      set { _step = value; }
    }
    private global::ProtoBuf.IExtension extensionObject;
    global::ProtoBuf.IExtension global::ProtoBuf.IExtensible.GetExtensionObject(bool createIfMissing)
      { return global::ProtoBuf.Extensible.GetExtensionObject(ref extensionObject, createIfMissing); }
  }
  
  [global::System.Serializable, global::ProtoBuf.ProtoContract(Name=@"NetLoginConfirm")]
  public partial class NetLoginConfirm : global::ProtoBuf.IExtensible
  {
    public NetLoginConfirm() {}
    
    private int _uid;
    [global::ProtoBuf.ProtoMember(1, IsRequired = true, Name=@"uid", DataFormat = global::ProtoBuf.DataFormat.TwosComplement)]
    public int uid
    {
      get { return _uid; }
      set { _uid = value; }
    }
    private string _sn = "";
    [global::ProtoBuf.ProtoMember(2, IsRequired = false, Name=@"sn", DataFormat = global::ProtoBuf.DataFormat.Default)]
    [global::System.ComponentModel.DefaultValue("")]
    public string sn
    {
      get { return _sn; }
      set { _sn = value; }
    }
    private global::ProtoBuf.IExtension extensionObject;
    global::ProtoBuf.IExtension global::ProtoBuf.IExtensible.GetExtensionObject(bool createIfMissing)
      { return global::ProtoBuf.Extensible.GetExtensionObject(ref extensionObject, createIfMissing); }
  }
  
}