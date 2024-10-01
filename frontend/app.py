import streamlit as st
import pandas as pd
import matplotlib.pyplot as plt

# Initialize session state for polls
if 'polls' not in st.session_state:
    st.session_state.polls = []

st.sidebar.title("Polling System")
st.sidebar.header("Navigation")
page = st.sidebar.selectbox("Go to", ["Create Poll", "Vote on Poll", "Poll Results"])

def display_poll(poll_data, poll_index):
    st.write(f"**Question:** {poll_data['question']}")
    st.write("**Options:**")
    for option, votes in zip(poll_data['options'], poll_data['votes']):
        st.write(f"- {option}: {votes} votes")
    if st.button("Edit", key=f"edit_{poll_index}"):
        st.session_state.edit_index = poll_index
        st.experimental_rerun()
    if st.button("Delete", key=f"delete_{poll_index}"):
        del st.session_state.polls[poll_index]
        st.experimental_rerun()

if page == "Create Poll":
    st.title("Create a Poll")
    question = st.text_input("Poll Question")
    options = []
    option_count = st.number_input("Number of Options", min_value=2, step=1, value=2)
    for i in range(option_count):
        options.append(st.text_input(f"Option {i+1}"))

    if st.button("Create Poll"):
        poll_data = {"question": question, "options": options, "votes": [0] * len(options)}
        st.session_state.polls.append(poll_data)
        st.write("Your poll has been created!")
        st.experimental_rerun()

    st.write("**Existing Polls:**")
    for i, poll in enumerate(st.session_state.polls):
        display_poll(poll, i)

elif page == "Vote on Poll":
    st.title("Vote on a Poll")
    for i, poll_data in enumerate(st.session_state.polls):
        st.write(f"**Question:** {poll_data['question']}")
        selected_option = st.radio("Select an Option", poll_data['options'], key=f"vote_{i}")
        if st.button("Vote", key=f"vote_button_{i}"):
            option_index = poll_data['options'].index(selected_option)
            poll_data['votes'][option_index] += 1
            st.session_state.polls[i] = poll_data
            st.write(f"Vote Submitted for: {selected_option}")

elif page == "Poll Results":
    st.title("Poll Results")
    for poll_data in st.session_state.polls:
        st.write(f"**Question:** {poll_data['question']}")
        st.write("**Options and Votes:**")
        for option, votes in zip(poll_data['options'], poll_data['votes']):
            st.write(f"- {option}: {votes} votes")

        # Plotting the results
        df = pd.DataFrame({
            'Options': poll_data['options'],
            'Votes': poll_data['votes']
        })
        fig, ax = plt.subplots()
        df.plot(kind='bar', x='Options', y='Votes', ax=ax, legend=False)
        plt.xticks(rotation=45)
        st.pyplot(fig)

st.markdown(
    """
    <style>
    .stButton>button {
        background-color: #1f77b4;
        color: white;
    }
    </style>
    """,
    unsafe_allow_html=True
)
