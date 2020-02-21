import numpy as np
import pandas as pd
import matplotlib.pyplot as plt

df = pd.read_csv("./all-survive/01A-hawk-log-2020-02-21T11:01:01.566.csv")
# /home/trygve/Documents/github/PredatorPreySimulation/Alle_dør .csv
#print(f"Age std: {df['Age'].std()}")
#print(f"Age avg: {df['Age'].mean()}")
#hawk_df = df.filter(like='', axis=0)

 
df2 = pd.read_csv("./all-survive2/pop.csv")
df3 = pd.read_csv("./hawkdies/pop.csv")
df4 = pd.read_csv("./bothDies/pop.csv")


hawkdies_hawk = pd.read_csv("./hawkdies/hawk.csv")
hawkdies_squirrel = pd.read_csv("./hawkdies/squirrel.csv")

bothDies_hawk = pd.read_csv("./bothDies/hawk.csv")
bothDies_squirrel = pd.read_csv("./bothDies/squirrel.csv")

alls_hawk = pd.read_csv("./all-survive2/hawk.csv")
alls_squirrel = pd.read_csv("./all-survive2/squirrel.csv")


print("hawk dies mean and avg")
for x in [hawkdies_hawk,hawkdies_squirrel]:
    print(f"animal {x.__name__}")
    print(f"Age std: {x['Age'].std()}")
    print(f"Age avg: {x['Age'].mean()}")

print("both dies mean and avg")
for x in [bothDies_hawk,bothDies_squirrel]:
    print(f"Age std: {x['Age'].std()}")
    print(f"Age avg: {x['Age'].mean()}")

print("all survive mean and avg")
for x in [alls_hawk,alls_squirrel]:
    print(f"Age std: {x['Age'].std()}")
    print(f"Age avg: {x['Age'].mean()}")

df3 = pd.read_csv("./hawkdies/pop.csv")
df4 = pd.read_csv("./bothDies/pop.csv")

#df2.cumsum()

#plt.figure()
#print(df2)

for x in [df2, df3, df4]:
    x["idx"] = range(len(x))
    ax=x.plot("idx", "SQUIRREL")
    x.plot("idx", "HAWK",secondary_y=True, ax=ax)
    #plt.show()
    
