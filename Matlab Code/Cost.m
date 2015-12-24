function y = Cost(x)
Npv = x(1);
Nw = x(2);
Solar_Cost = Npv*1500*1000; %Assume 1 unit = 1MW
Wind_Cost = Nw*1300*5*1000; %Assume 1 unit = 5MW = 5000kW

kW = (Npv*1000 + Nw*5*1000);
 
% O_M = 0;
% r = 0.035;
% i = 0.03;
% 
% for i=1:25
%     O_M = O_M+((1+r)/(1+i))^i;
% end
% 
% O_M = O_M*32*kW;

y = Solar_Cost + Wind_Cost;

N = [x(1),x(2),kW,y];

dlmwrite('Run10_Cost.csv',N,'delimiter',',','-append')
%y = Solar_Cost + Wind_Cost + O_M/Energy(x);
end

