import pandas as pd
gsheetId="1Tfsie3jgw-FrZC03vUB4a2jot8oJfHpR3ftF9jnYT2M"
sheet_name="Sheet1"

gsheet_Url="https://docs.google.com/spreadsheets/d/{}/gviz/tq?tqx=out:csv&sheet={}".format(gsheetId,sheet_name)
df=pd.read_csv(gsheet_Url,header=1)
def application_recieved():
    rows,column=df.shape
    return f"   Number of Applicant: {rows+1}"
def get_application_details():
    return df.to_csv(index=False)

def class10_avg():
    df=pd.read_csv(gsheet_Url)
    return f"{df['X Marks'].mean()}%"
def class12_avg():
    df=pd.read_csv(gsheet_Url)
    return f"{df['XII Marks'].mean()}%"

def Carricular_activities():
    df=pd.read_csv(gsheet_Url)
    return f"{df[df['Carricular Activities'] != 'N.A'].shape[0]} Students"