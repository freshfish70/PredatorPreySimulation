import numpy as np
import pandas as pd

df = pd.read_csv("/home/trygve/Documents/github/PredatorPreySimulation/Alle_dør.csv")
# /home/trygve/Documents/github/PredatorPreySimulation/Alle_dør .csv
print(df["Age"].std())
hawk_df = df.filter(like='HAWK', axis=0)