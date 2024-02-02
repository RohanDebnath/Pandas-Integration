import pandas as pd

gsheetId = "1Tfsie3jgw-FrZC03vUB4a2jot8oJfHpR3ftF9jnYT2M"
sheet_name = "Sheet1"
length = 0
filtered_df = pd.DataFrame()
filteredActivity_df = pd.DataFrame()
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
    return f"{df_temp['X Marks'].mean()}%"


def class12_avg():
    df_temp = pd.read_csv(gsheet_Url)
    return f"{df_temp['XII Marks'].mean()}%"


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
    merged_df = pd.concat([filtered_df, filteredActivity_df], ignore_index=True)

    # Drop duplicates based on all columns
    merged_df = merged_df.drop_duplicates()
    length=merged_df.shape[0]
    return merged_df.to_csv(index=False)



# Example usage:
# filter_students(80, 85, 3)
# select_students_by_activity("Swimming", 2)
# merged_data = merge_dataframes()
# print(merged_data)
