/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication2.pciLogUtil;


public enum PCI_Constants_Req {
    Direction(1), TypeOfTransaction(2), Time(3), Type(4), Fmt(5), TC(6), Attr_IDO(7), TH(8), TD(9), EP(10), Attr2(11), AT(12), Length(13), ReqID(14), Tag(15), LastDWBE(16), FirstDWBE(17),
    BusNo(18), DeviceNo(19), FunctionNo(20), ExtRegNo(21), RegisterNo(22), Address(23), PH(24), ECRC(25), MESSAGECODE(26), StreeingTag(27), ;
    
   
   // Direction(1), TypeOfTransaction(2), Time(3), Type(4), Fmt(5), TC(6), Attr_IDO(7), TH(8), TD(9), EP(10), Attr2(11), AT(12), Length(13), CompID(14), ComplStatus(15), BCM(16), ByteCount(17), 
    //ReqID(18), Tag(19), OwerAddress(20), ECRC(21);
    
    private final int index;

    private PCI_Constants_Req(int index) {
        this.index = index;
    }
    public int getIndex() {
        return this.index;
    }
 }
