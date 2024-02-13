import pandas as pd
import ssl
import smtplib
from email.message import EmailMessage

gsheetId = "1Tfsie3jgw-FrZC03vUB4a2jot8oJfHpR3ftF9jnYT2M"
sheet_name = "Sheet1"
length = 0
filtered_df = pd.DataFrame()
filteredActivity_df = pd.DataFrame()
merged_df=pd.DataFrame()
gsheet_Url = "https://docs.google.com/spreadsheets/d/{}/gviz/tq?tqx=out:csv&sheet={}".format(gsheetId, sheet_name)
df = pd.read_csv(gsheet_Url, header=1)


def application_received():
    rows, column = df.shape
    return f"   Number of Applicant: {rows + 1}"


def get_application_details():
    return df.to_csv(index=False)


def get_length():
    return f"Number of Student Selected: {length}"


def class10_avg():
    df_temp = pd.read_csv(gsheet_Url)
    return "{:.2f}%".format(df_temp['X Marks'].mean())


def class12_avg():
    df_temp = pd.read_csv(gsheet_Url)
    return "{:.2f}%".format(df_temp['XII Marks'].mean())



def curricular_activities():
    df_temp = pd.read_csv(gsheet_Url)
    return f"{df_temp[df_temp['Carricular Activities'] != 'N.A'].shape[0]} Students"


def filter_students(class_x_cutoff, class_xii_cutoff, limit_students=None):
    global filtered_df
    df_temp = pd.read_csv(gsheet_Url)
    df_temp = df_temp.sort_values(by=['X Marks', 'XII Marks'], ascending=False)
    filtered_df = df_temp[(df_temp['X Marks'] > class_x_cutoff) & (df_temp['XII Marks'] > class_xii_cutoff)]

    if limit_students is not None:
        filtered_df = filtered_df.head(limit_students)
    return filtered_df.to_csv(index=False)


def select_students_by_activity(activity, num_students=None):
    global filteredActivity_df
    df_temp = pd.read_csv(gsheet_Url)
    df_temp = df_temp.sort_values(by=['X Marks', 'XII Marks'], ascending=False)
    filteredActivity_df = df_temp[df_temp['Carricular Activities'] == activity]

    if num_students is not None:
        filteredActivity_df = filteredActivity_df.head(num_students)
    return filteredActivity_df.to_csv(index=False)


def merge_dataframes():
    global filtered_df
    global filteredActivity_df
    global length
    global merged_df
    merged_df = pd.concat([filtered_df, filteredActivity_df], ignore_index=True)

    # Drop duplicates based on all columns
    merged_df = merged_df.drop_duplicates()
    length=merged_df.shape[0]
    return merged_df.to_csv(index=False)

#Email part

# email_sender = 'rohandn887@gmail.com'
# email_password = 'zflo fybk ylar kftn'
# subject = "Congratulations!! On Selection"

def send_emails():
    merge_dataframes()
    sender_email='rohandn887@gmail.com'
    sender_password='zflo fybk ylar kftn'
    subject="Congratulations!! On Selection"
    global merged_df
    context = ssl.create_default_context()
    
    for index, row in merged_df.iterrows():
        receiver_email = row['Email']
        student_name = row['Student Name']
        if receiver_email == 'abc@gmail.com':
            continue  # Skip sending email to this address
        
        body = f"Dear {student_name} a warm Congratulations as you have selected for our institution."
        
        em = EmailMessage()
        em['From'] = sender_email
        em['To'] = receiver_email
        em['Subject'] = subject
        em.set_content(body)
        
        with smtplib.SMTP_SSL('smtp.gmail.com', 465, context=context) as smtp:
            smtp.login(sender_email, sender_password)
            smtp.sendmail(sender_email, receiver_email, em.as_string())
            print("Done")



# Example usage:
# print(filter_students(80, 85, 5))
# send_emails()
