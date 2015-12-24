
A=[];
B=[]; 
Aeq=[];
Beq=[];
LB=[0,0];
UB=[];
IntCon = [1,2];

ga(@Cost,2,A,B,Aeq,Beq,LB,UB,@Constraint,IntCon)