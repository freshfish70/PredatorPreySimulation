import numpy as np
import pandas as pd
import matplotlib.pyplot as plt

df = pd.read_csv("/home/trygve/Documents/github/PredatorPreySimulation/all-survive/01A-hawk-log-2020-02-21T11:01:01.566.csv")
# /home/trygve/Documents/github/PredatorPreySimulation/Alle_dør .csv
print(f"Age std: {df['Age'].std()}")
print(f"Age avg: {df['Age'].mean()}")
#hawk_df = df.filter(like='', axis=0)

 
df2 = pd.read_csv("/home/trygve/Documents/github/PredatorPreySimulation/all-survive/01A-population-log-2020-02-21T11:01:01.566.csv")
df2.cumsum()

#plt.figure()
print(df2)
df2["idx"] = range(len(df2))
ax=df2.plot("idx", "SQUIRREL")
df2.plot("idx", "HAWK",secondary_y=True, ax=ax)
plt.show()