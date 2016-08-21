package javafxapplication2.pciLogUtil;

public enum PCI_Constants_Com {
	
	Direction(1), TypeOfTransaction(2), Time(3), Type(4), Fmt(5), TC(6), Attr_IDO(7), TH(8), TD(9), EP(10), Attr2(11), AT(12), Length(13), CompID(14), ComplStatus(15), BCM(16), ByteCount(17), 
    ReqID(18), Tag(19), OwerAddress(20), ECRC(21);
    
    private final int index;

    private PCI_Constants_Com(int index) {
        this.index = index;
    }
    public int getIndex() {
        return this.index;
    }
}
