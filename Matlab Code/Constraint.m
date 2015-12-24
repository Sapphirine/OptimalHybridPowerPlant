   
function [c,ceq] = Constraint(x)

% Npv = x(1);
% Nw = x(2);

% Reading Data
w= csvread('LongIsland_wind_2010.csv');
w = w/4; %MW value for 5MW wtg
% w= transpose(w);
s = xlsread('PV Hourly Power Output.xlsx');
s = s*10/1000; %Gives value in MW for a 1MW array
Demand = csvread('nys_2011_demand.csv');

Supply = x(1)*s+x(2)*w;

Excess =Supply-Demand;
for i = 1:8760
    if Excess(i)<0
        Excess(i) = 0;
    end
end

Useful_Power = Supply-Excess;

N = [x(1),x(2),sum(Demand),sum(Useful_Power),sum(Supply),sum(Excess)];

dlmwrite('Run10_Constraint40_10.csv',N,'delimiter',',','-append')

c = [0.4*sum(Demand)-sum(Useful_Power) ;
    sum(Excess)-0.1*sum(Supply)];
ceq = [];

% Npv = floor(mean(l)/mean(s));
% Nw = floor(mean(l)/mean(w));

% Npv = 1:2500;
% Nw = 1:2500;

