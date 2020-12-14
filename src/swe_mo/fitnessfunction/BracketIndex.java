package swe_mo.fitnessfunction;

public class BracketIndex {
	public int roundOpen = -1; // (
	public int roundClose = -1; // )
	public int curlyOpen = -1; // {
	public int curlyClose = -1; // }
	
	public void findNextBrackets(String s) {
		roundOpen = s.indexOf("(");
		roundClose = s.indexOf(")");
		curlyOpen = s.indexOf("{");
		curlyClose = s.indexOf("}");
	}
	

	public boolean roundOpenFirst() {
		boolean isfirst = roundOpen > -1;
		if(roundClose > -1)
			isfirst = isfirst && (roundOpen < roundClose);
		if(curlyOpen > -1)
			isfirst = isfirst && (roundOpen < curlyOpen);
		if(curlyClose > -1)
			isfirst = isfirst && (roundOpen < curlyClose);		
		return isfirst;
	}
	public boolean roundCloseFirst() {
		boolean isfirst = roundClose > -1;
		if(roundOpen > -1)
			isfirst = isfirst && (roundClose < roundOpen);
		if(curlyOpen > -1)
			isfirst = isfirst && (roundClose < curlyOpen);
		if(curlyClose > -1)
			isfirst = isfirst && (roundClose < curlyClose);		
		return isfirst;
	}
	public boolean curlyOpenFirst() {
		boolean isfirst = curlyOpen > -1;
		if(roundOpen > -1)
			isfirst = isfirst && (curlyOpen < roundOpen);
		if(roundClose > -1)
			isfirst = isfirst && (curlyOpen < roundClose);
		if(curlyClose > -1)
			isfirst = isfirst && (curlyOpen < curlyClose);		
		return isfirst;
	}
	public boolean curlyCloseFirst() {
		boolean isfirst = curlyClose > -1;
		if(roundOpen > -1)
			isfirst = isfirst && (curlyClose < roundOpen);
		if(roundClose > -1)
			isfirst = isfirst && (curlyClose < roundClose);
		if(curlyOpen > -1)
			isfirst = isfirst && (curlyClose < curlyOpen);		
		return isfirst;
	}
	public boolean noBrackets() {
		return roundOpen == -1  && roundClose == -1 && curlyClose == -1 && curlyOpen == -1;
	}
	
	@Override
	public String toString() {
		return "( at "+roundOpen+" -- ) at "+roundClose+" -- { at "+curlyOpen+" -- } at "+curlyClose;
	}
}
